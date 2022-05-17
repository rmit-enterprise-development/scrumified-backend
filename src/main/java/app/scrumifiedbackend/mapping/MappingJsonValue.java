package app.scrumifiedbackend.mapping;

import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.http.converter.json.MappingJacksonValue;

import java.util.*;

public class MappingJsonValue<T> {
    public MappingJacksonValue returnJson(HashMap<String, Set<String>> filter, T entity) {
        HashMap<String, SimpleBeanPropertyFilter> hashMap = new HashMap<>();
        for (String key : filter.keySet()) {
            hashMap.put(key, SimpleBeanPropertyFilter.filterOutAllExcept(filter.get(key)));
        }
        SimpleFilterProvider simpleFilterProvider = new SimpleFilterProvider();
        for (String key : hashMap.keySet()) {
            simpleFilterProvider.addFilter(key, hashMap.get(key));
        }

        MappingJacksonValue mapping = new MappingJacksonValue(entity);
        mapping.setFilters(simpleFilterProvider);
        return mapping;
    }
}
