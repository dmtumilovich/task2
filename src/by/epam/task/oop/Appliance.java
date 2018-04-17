package by.epam.task.oop;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Appliance {
    private String name;
    private Map<String, Object> options;

    public Appliance() {
        options = new LinkedHashMap<>();
    }

    public Appliance(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addOption(String optionName, Object value) {
        options.put(optionName, value);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(name).append(": ");
        Iterator<Map.Entry<String, Object>> iterator = options.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            builder.append(entry.getKey()).append(" = ").append(entry.getValue()).append("  ");
        }
        return builder.toString();
    }
}
