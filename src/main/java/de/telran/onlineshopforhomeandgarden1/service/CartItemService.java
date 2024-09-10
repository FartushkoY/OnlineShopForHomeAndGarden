package de.telran.onlineshopforhomeandgarden1.service;

import de.telran.onlineshopforhomeandgarden1.mapper.CartItemMapper;
import de.telran.onlineshopforhomeandgarden1.repository.CartItemRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartItemService {
    private static final Logger logger = LogManager.getLogger(CartItemService.class);
    private final CartItemRepository repository;
    private final CartItemMapper mapper;

    @Autowired
    public CartItemService(CartItemRepository repository, CartItemMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

}
