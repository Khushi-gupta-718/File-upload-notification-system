package dev.khushi;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SimpleHandlerTest {

    private SimpleHandler simpleHandler;

   @Mock
   Context context;

    @Mock
    LambdaLogger logger;

    @BeforeEach
    public void setup(){
        Mockito.when(context.getLogger()).thenReturn(logger);

        doAnswer(call -> {
            System.out.println((String)call.getArgument(0));
            return null;
        }).when(logger).log(anyString());

        simpleHandler=new SimpleHandler();
    }
    void shouldReturnUppercaseOdInput(){
       when(context.getFunctionName()).thenReturn("handleRequest");

       Assertions.assertEquals("hello, world" , simpleHandler.handleRequest("hello, world" , context));
    }

}