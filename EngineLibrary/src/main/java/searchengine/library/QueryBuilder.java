package searchengine.library;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;

public class QueryBuilder {

    public static String getFormatedQueryToExec(String initialQuery, List<String> paramsToPass){
        //a
        //a & b
        //a & (b | c)
        String finalQuery = "";
        String query = initialQuery;

        query = query.replace("(", "").replace(")", "");
        String[] argsArray = splitQueryString(initialQuery);
        if (argsArray == null){
            throw new InvalidParameterException("A token was not provided to search for.");
        }

        int argsCount = argsArray.length;

        finalQuery = createSingleArgQuery(argsArray[0], paramsToPass);

        if (argsCount == 3){
            String logicOperator = argsArray[1];
            String nextValue = argsArray[2];
            return createQueryForDoubleParameters(finalQuery, logicOperator, nextValue, paramsToPass);
        }



        return null;
    }

    private static String createQueryForDoubleParameters(String query, String secondArg, String logicOperator, List<String> parameters){
        parameters.add(secondArg);

        if (logicOperator.equals("|")){
            query = AddOrClauseForNextArg(query);
        }
        else{
            // operator &
            query = AddAndClauseForNextArg(query);
        }

        return query;
    }

    private static String AddAndClauseForNextArg(String initialQuery){
        return initialQuery.concat(" \nINTERSECT\nSELECT DISTINCT DocumentId FROM Tokens WHERE Content = ?");
    }

    private static String AddOrClauseForNextArg(String initialQuery){
        return initialQuery.concat(" OR Content = ?");
    }

    private static String createSingleArgQuery(String value, List<String> parameters){
        parameters.add(value);
        return "SELECT DISTINCT DocumentId FROM Tokens WHERE Content = ?";
    }

    private static String[] splitQueryString(String query){
        var args = query.split(" ");
        int count = args.length;

        if (count < 2){
            return null;
        }

        //remove the first empty entry that is created from the split method
        String[] newArgs = new String[count - 1];
        for (int i = 1; i < count; i++){
            newArgs[i - 1] = args[i];
        }

        return newArgs;
    }
}
