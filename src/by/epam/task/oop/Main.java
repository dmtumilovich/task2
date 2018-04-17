package by.epam.task.oop;

import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {

        String[] criteria = {"OVERALL_CAPACITY=300", "WIDTH=70","FREEZER_CAPACITY=10"};
        String[] criteria_1 = {"Laptop"};
        String[] criteria_2 = {"OS=Windows", "MEMORY_ROM>4000"};
        String[] criteria_3 = {"OVERALL_CAPACITY<=310", "WIDTH>=70", "WIDTH<=79"};
        String[] criteria_4 = {"OS=Windows"};
        String[] criteria_5 = { "FREQUENCY_RANGE=3-4"};
        String[] criteria_6 = {"TextBook", "TITLE=Java: промышленное программирование"};

        File file = new File(".", "appliances_db.txt");
        String[] things = fileIStream(file);

        printResult(criteriaSearch(things, criteria_1));
        printResult(criteriaSearch(things, criteria));
        printResult(criteriaSearch(things, criteria_2));
        printResult(criteriaSearch(things, criteria_3));
        printResult(criteriaSearch(things, criteria_4));
        printResult(criteriaSearch(things, criteria_5));
        printResult(criteriaSearch(things, criteria_6));
    }

    public static void printResult(List<Appliance> appliances) {
        if(appliances.size() == 0) {
            System.out.println("No matches!\n");
            return;
        }
        for(Appliance appliance : appliances) {
            System.out.println(appliance);
        }
        System.out.println();
    }

    public static String[] fileIStream(File file){
        FileInputStream fis = null;
        String[] things = null;
        try {
            fis = new FileInputStream(file);

            byte [] b = new byte[fis.available()];
            fis.read(b, 0, fis.available());
            String text = new String(b);
            things = text.split("\\r\\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (fis != null)
                    fis.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return things;
    }

    public static List<Appliance> criteriaSearch(String[] things, String[] criteria){

        List<Appliance> matches = new ArrayList<>();

        for(String thing:things) {
            for (int i = 0; i < criteria.length; i++) {
                boolean bool = compareNum(thing, criteria[i]);
                if ((i == criteria.length - 1) && (bool == true)) {
                    Appliance appliance = getApplianceFromString(thing);
                    matches.add(appliance);
                }
                if (!bool)
                    break;
            }
        }

        return matches;
    }

    public static boolean compareNum(String thing, String criterion){
        String sign = getSignPart(criterion);
        String critWordPart = getCritWordPart(criterion);
        float criterionNum = getCriterionNum(criterion);
        float thingNum = getThingCriterionNum(critWordPart + "=", thing);
        if (sign == null){
            Matcher matcher_0 = Pattern.compile(".*" + criterion + ".*").matcher(thing);
            if (matcher_0.matches())
                return true;
            else return false;
        }

        Matcher matcher_0 = Pattern.compile(".*" + critWordPart + ".*").matcher(thing);
        if (!matcher_0.matches())
            return false;

        if (sign.equals("<=")){
            if (thingNum <= criterionNum)
                return true;
            else return false;
        }

        if (sign.equals("<")){
            if (thingNum < criterionNum)
                return true;
            else return false;
        }

        if (sign.equals(">=")){
            if (thingNum >= criterionNum)
                return true;
            else return false;
        }

        if (sign.equals(">")){
            if (thingNum > criterionNum)
                return true;
            else return false;
        }

        if (getSignPart(criterion).equals("=")){
            Matcher matcher = Pattern.compile(".*" + criterion + ",.*").matcher(thing);
            if (matcher.matches())
                return true;
            else return false;
        }
        return false;
    }

    public static String getCritWordPart(String criterion) {
        String critWordPart = null;
        Matcher matcher = Pattern.compile("[^<>=]+(?=[<>=]+)").matcher(criterion);
        while (matcher.find())
            critWordPart = matcher.group();
        return critWordPart;
    }

    public static String getSignPart(String criterion) {
        String sign = null;
        Matcher matcher = Pattern.compile("[<>=]+").matcher(criterion);
        while (matcher.find())
            sign = matcher.group();
        return sign;
    }

    public static float getCriterionNum(String criterion) {
        float criterionNum = 0;
        Matcher matcher = Pattern.compile("[0-9]+\\.?[0-9]*").matcher(criterion);
        while (matcher.find())
            criterionNum = Float.parseFloat(matcher.group());
        return criterionNum;
    }

    public static float getThingCriterionNum(String critWordPart, String thing) {
        float thingCriterionNum = 0;
        Matcher matcher = Pattern.compile("(?<=" + critWordPart + ")[0-9]+\\.?[0-9]*").matcher(thing);
        while (matcher.find())
            thingCriterionNum = Float.parseFloat(matcher.group());
        return thingCriterionNum;
    }

    public static Appliance getApplianceFromString(String line) {
        Appliance appliance = new Appliance();
        String name = line.split("\\W")[0];
        appliance.setName(name);
        String optionsString = line.replaceFirst(name + "\\W+", "");
        for(String option : optionsString.split(",")) {
            String str[] = option.split("=");
            appliance.addOption(str[0].trim(), str[1].replaceAll(";", ""));
        }
        return appliance;
    }
}
