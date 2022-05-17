package app.scrumifiedbackend.mapping;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ModelMapperSingleton {
    public static ModelMapper modelMapper;

    public ModelMapperSingleton(ModelMapper modelMapper) {
        ModelMapperSingleton.modelMapper = modelMapper;
    }
}
