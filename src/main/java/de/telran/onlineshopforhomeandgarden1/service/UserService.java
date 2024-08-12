package de.telran.onlineshopforhomeandgarden1.service;

import de.telran.onlineshopforhomeandgarden1.mapper.UserMapper;
import de.telran.onlineshopforhomeandgarden1.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger logger = LogManager.getLogger(UserService.class);
//    private final UserRepository repository;
//    private final UserMapper mapper;
//
//    @Autowired
//    public UserService(UserRepository repository, UserMapper mapper) {
//        this.repository = repository;
//        this.mapper = mapper;
//    }
}
