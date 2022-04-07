package searchengine.library;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QueryBuilder {

    public static String getFormatedQueryToExec(String initialQuery, List<String> paramsToPass){
        //a
        //a & b
        //a & (b | c)
        String finalQuery;
        String query = initialQuery;

        query = query.replace("(", "").replace(")", "");
        List<String> arguments = splitQueryString(query);
        if (arguments == null){
            throw new InvalidParameterException("A token was not provided to search for.");
        }

        int argsCount = arguments.size();

        finalQuery = createSingleArgQuery(arguments.get(0), paramsToPass);

        if (argsCount == 3){
            String logicOperator = arguments.get(1);
            String nextValue = arguments.get(2);
            return createQueryForDoubleParameters(finalQuery,nextValue, logicOperator, paramsToPass);
        }

        if (argsCount == 5){
            return HandleMultipleParameters(initialQuery, paramsToPass);
        }

        if (argsCount > 5){
            throw new InvalidParameterException("Query is too long to process.");
        }

        return finalQuery;
    }

    private static String HandleMultipleParameters(String initialQuery, List<String> parameters){
        boolean onlyAnds = !initialQuery.contains("|");
        boolean onlyOrs = !initialQuery.contains("&");
        String finalQuery = "";
        parameters.clear();

        if(onlyAnds || onlyOrs){
            initialQuery = removeOperatorsSymbolsfromQuery(initialQuery);

            var splittedArgs = splitQueryString(initialQuery);
            String par1 = splittedArgs.get(0);
            String par2 = splittedArgs.get(1);
            String par3 = splittedArgs.get(2);

            if (onlyAnds){
                finalQuery = getOnlyDoubleAndsQuery(par1, par2, par3, parameters);
            }

            if (onlyOrs){
                finalQuery = giveOnlyDoubleOrsQuery(par1, par2, par3, parameters);
            }
        }
        else{
            finalQuery = calculateMixedOperatorsQuery(initialQuery, parameters);
        }

        return finalQuery;
    }

    private static String calculateMixedOperatorsQuery(String initialQuery, List<String> parameters){
        String query;
        String par1, par2 = "", par3 , par4 = "";
        String a , b , c ;

        query = """
                    SELECT DocumentId FROM Tokens
                    WHERE Content IN (@par1, @par2)
                    INTERSECT
                    SELECT DocumentId FROM Tokens
                    WHERE Content IN (@par3, @par4)
                """;

        int indexOfAndOperator = initialQuery.indexOf("&");
        int indexOfOrOperator = initialQuery.indexOf("|");
        int indexOfOpeningBrace = initialQuery.indexOf("(");
        int indexOfClosingBrace = initialQuery.indexOf(")");

        initialQuery = removeOperatorsSymbolsfromQuery(initialQuery);
        var listArgs = splitQueryString(initialQuery);

        a = listArgs.get(0);
        b = listArgs.get(1);
        c = listArgs.get(2);

        //  mode (a & b) | c ; mode a | (b & c)
        if (indexOfAndOperator > indexOfOpeningBrace && indexOfAndOperator < indexOfClosingBrace){
            if (indexOfClosingBrace < indexOfOrOperator){
                // mode (a & b) | c = (a | c) & (b | c)
                par1 = a;
                par2 = c;
                par3 = b;
                par4 = c;
            }
            else{
                // mode a | (b & c) = (a | b) & (a | c)
                par1 = a;
                par2 = b;
                par3 = a;
                par4 = c;
            }
        }
        else{
            // mode (a | b) & c ; // mode a & (b | c)
            if (indexOfClosingBrace < indexOfAndOperator)
            {
                // mode (a | b) & c    =   (a | b) & (c | d) ; d is null
                par1 = a;
                par2 = b;
                par3 = c;
            }
            else
            {
                // mode a & (b | c)   =    (a | b) & (b | c) ; b is null
                par1 = a;
                par3 = b;
                par4 = c;
            }
        }

        parameters.add(par1);
        parameters.add(par2);
        parameters.add(par3);
        parameters.add(par4);

        return query;
    }

    private static  String giveOnlyDoubleOrsQuery(String par1, String par2, String par3, List<String> parameters){
        parameters.add(par1);
        parameters.add(par2);
        parameters.add(par3);

        return """
                SELECT DISTINCT DocumentId from Tokens
                WHERE Content IN (?, ?, ?)
                """;
    }

    private static String getOnlyDoubleAndsQuery(String par1, String par2, String par3, List<String> parameters){
        parameters.add(par1);
        parameters.add(par2);
        parameters.add(par3);

        return """
                SELECT DocumentId FROM Tokens WHERE Content = ?
                INTERSECT
                SELECT DocumentId FROM Tokens WHERE Content = ?
                INTERSECT
                SELECT DocumentId FROM Tokens WHERE Content = ?""";
    }

    private static String removeOperatorsSymbolsfromQuery(String query){
        return query.replace("&", "")
                .replace("|", "")
                .replace("(", "")
                .replace(")", "");
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

    /*
    Splits the string with the given arguments by the whitespace character and returns all the
    arguments provided by the string
     */
    private static List<String> splitQueryString(String query){
        var args = query.split("\\ ", -1);
        int count = args.length;
        List<String> arguments = new ArrayList<>();

        if (count == 0){
            return null;
        }

        // scan for empty entries generated by the split method and remove them
        for (String item:args) {
            if (!item.isBlank()){
                arguments.add(item);
            }
        }
        if (arguments.size() == 0){
            return null;
        }

        return arguments;
    }
}
