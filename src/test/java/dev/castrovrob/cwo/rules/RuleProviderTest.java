package dev.castrovrob.cwo.rules;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class RuleProviderTest {

    @InjectMocks
    private RuleProvider ruleProvider;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        ruleProvider = new RuleProvider();
    }

    @Test
    void testFetchRules_Success() throws Exception {
        String jsonRules = """
                [
                    {"type": "hour", "priority": 0, "message": "bonk"},
                    {"type": "minute", "priority": 1, "message": "tock"},
                    {"type": "second", "priority": 2, "message": "tick"}
                ]
                """;

        InputStream inputStream = new ByteArrayInputStream(jsonRules.getBytes());
        RuleProvider spyRuleProvider = Mockito.spy(ruleProvider);
        doReturn(inputStream).when(spyRuleProvider).getClassLoaderResource("rules.json");
        List<Rule> mockRules = List.of(
                new Rule(0, "hour", "bong"),
                new Rule(1, "minute", "tock"),
                new Rule(2, "second", "tick")
        );
        Mockito.lenient().when(objectMapper.readValue(any(InputStream.class), any(TypeReference.class)))
                .thenReturn(mockRules);
        spyRuleProvider.fetchRules();

        Assertions.assertNotNull(spyRuleProvider.getRules(), "Rules should be loaded");
        Assertions.assertEquals(3, spyRuleProvider.getRules().size(), "Should have 3 rules");
        Assertions.assertEquals("hour", spyRuleProvider.getRules().get(0).getType(), "First rule should be 'hour'");
        Assertions.assertEquals("minute", spyRuleProvider.getRules().get(1).getType(), "Second rule should be 'minute'");
        Assertions.assertEquals("second", spyRuleProvider.getRules().get(2).getType(), "Third rule should be 'second'");
    }

    @Test
    void testFetchRules_FileNotFound_ThrowsException() {
        RuleProvider spyRuleProvider = Mockito.spy(ruleProvider);
        doReturn(null).when(spyRuleProvider).getClassLoaderResource("rules.json");
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, spyRuleProvider::fetchRules);
        Assertions.assertTrue(exception.getMessage().contains("Failed to load rules from resources"));
    }

    @Test
    void testFetchRules_InvalidJson_ThrowsException() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("INVALID JSON".getBytes());
        RuleProvider spyRuleProvider = Mockito.spy(ruleProvider);
        doReturn(inputStream).when(spyRuleProvider).getClassLoaderResource("rules.json");
        Mockito.lenient().when(objectMapper.readValue(any(InputStream.class), any(TypeReference.class)))
                .thenThrow(new IOException("Invalid JSON format"));
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, spyRuleProvider::fetchRules);
        Assertions.assertTrue(exception.getMessage().contains("Failed to load rules from resources"));
    }
}