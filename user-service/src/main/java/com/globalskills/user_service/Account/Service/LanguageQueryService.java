package com.globalskills.user_service.Account.Service;

import com.globalskills.user_service.Account.Entity.Language;
import com.globalskills.user_service.Account.Repository.LanguageRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageQueryService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    LanguageRepo languageRepo;

    public List<Language> getAll(){
        return languageRepo.findAll();
    }
}
