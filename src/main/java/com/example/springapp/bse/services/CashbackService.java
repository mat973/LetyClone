package com.example.springapp.bse.services;

import com.example.springapp.bse.messages.ItemRefundedRequest;
import com.example.springapp.bse.messages.ItemRefundedResponse;
import com.example.springapp.bse.messages.ItemsNonrefundableRequest;
import com.example.springapp.bse.messages.ItemsNonrefundableResponse;
import com.example.springapp.letyclone.messages.GetUserRequest;
import com.example.springapp.letyclone.messages.GetUserResponse;
import com.example.springapp.letyclone.messages.UserCreatedOrderRequest;
import com.example.springapp.letyclone.messages.UserCreatedOrderResponse;
import com.example.springapp.letyclone.model.*;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class CashbackService {
    @Autowired
    ShopRepository shopRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRepository itemRepository;
    @Transactional
    public void withdraw(Integer userId) {
        if (userId == null) throw new RuntimeException("userId не может быть null");
        User user = userRepository.findById(userId.intValue());
        if (user == null) throw new RuntimeException("Пользователь не был найден");
        user.setBalance(0.0);
        userRepository.save(user);
    }

    @Transactional
    public void userCreatedOrder(String shopName, int userId, List<UserCreatedOrderRequest.Item> items) {
        Shop shop = shopRepository.findByName(shopName);
        User user = userRepository.findById(userId);

        if (shop == null || user == null) throw new RuntimeException("Ошибка в базе данных");

        for (UserCreatedOrderRequest.Item reqItem : items) {
            Item item = new Item(user, shop, reqItem.cashback, reqItem.canBeRefunded, reqItem.refunded, reqItem.itemId);
            itemRepository.save(item);
        }
    }

    @Transactional
    public void itemRefunded(int userId, String shopName, int shopOrderedItemId) {
        Shop shop = shopRepository.findByName(shopName);
        List<Item> items = itemRepository.findAllByUserIdAndShopIdAndShopOrderedItemId(userId, shop.getId(), shopOrderedItemId);
        if (!(items.size() == 1)) throw new RuntimeException("Ошибка в базе данных");
        if (!items.get(0).getCanBeRefunded()) throw new RuntimeException("Товар не может быть возвращен");
        items.get(0).setRefunded(true);
        itemRepository.save(items.get(0));
    }
    
    @Transactional
    public void itemsNonrefundable(int userId, String shopName, List<Integer> itemIds) {
        User user = userRepository.findById(userId);
        Shop shop = shopRepository.findByName(shopName);

        for (int itemId: itemIds) {
            List <Item> items = itemRepository.findAllByUserIdAndShopIdAndShopOrderedItemId(userId, shop.getId(), itemId);
            if (items.size() != 1) throw new RuntimeException("Ошибка в базе данных");

            if (!items.get(0).getCanBeRefunded()) {
                throw new RuntimeException("Товар уже отмечен, как невозвратный");
            }
            items.get(0).setCanBeRefunded(false);
            itemRepository.save(items.get(0));
            if (!items.get(0).getRefunded()) {
                user.setBalance(user.getBalance() + items.get(0).getCashback());
                userRepository.save(user);
            }
        }
    }
}
