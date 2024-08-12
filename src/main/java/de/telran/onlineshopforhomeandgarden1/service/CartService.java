package de.telran.onlineshopforhomeandgarden1.service;

import de.telran.onlineshopforhomeandgarden1.mapper.CartMapper;
import de.telran.onlineshopforhomeandgarden1.repository.CartRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    private static final Logger logger = LogManager.getLogger(CartService.class);
//    private final CartRepository repository;
//    private final CartMapper mapper;
//
//    @Autowired
//    public CartService(CartRepository repository, CartMapper mapper) {
//        this.repository = repository;
//        this.mapper = mapper;
//    }
}
