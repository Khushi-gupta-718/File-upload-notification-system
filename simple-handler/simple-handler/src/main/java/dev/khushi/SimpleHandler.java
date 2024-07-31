package dev.khushi;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class SimpleHandler implements RequestHandler<String,String>{
    public String handleRequest(String input, Context context){
        LambdaLogger logger = context.getLogger();
        logger.log("function " + context.getFunctionName() + " called");
        return input.toUpperCase();
    }
}
