package com.ibm.practica.shop.service;
import com.ibm.practica.shop.model.dto.AddUserDTO;
import com.ibm.practica.shop.model.dto.GetUserDTO;
import com.ibm.practica.shop.model.dto.UserDTO;
import com.ibm.practica.shop.model.entity.Product;
import com.ibm.practica.shop.model.entity.User;
import com.ibm.practica.shop.repository.ProductRepository;
import com.ibm.practica.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;


@Service
@Log4j2
@RequiredArgsConstructor

public class UserService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public ResponseEntity<String> addUser(AddUserDTO addUserDTO){

        User userToBeSaved = User.builder()
                .firstName(addUserDTO.getFirst_name().toLowerCase())
                .lastName(addUserDTO.getLast_name().toLowerCase())
                .email(addUserDTO.getEmail().toLowerCase())
                .password(addUserDTO.getPassword().toLowerCase())
                .basket(addUserDTO.getBasketDTO().getBasket())
                .build();
       if(userRepository.count() == 0){
            userRepository.save(userToBeSaved);
            return ResponseEntity.accepted().body("Added Succesfully!");
        }

       if(userRepository.checkIfUserExists(addUserDTO.getFirst_name().toLowerCase(), addUserDTO.getLast_name().toLowerCase(), addUserDTO.getEmail().toLowerCase())){
            return ResponseEntity.badRequest().body("User already exists!");
        }
        userRepository.save(userToBeSaved);
        return ResponseEntity.accepted().body("User Added!");
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public ResponseEntity<String> deleteUserByEmail(String email){
        if(userRepository.findUserByEmail(email.toLowerCase()).isPresent()){
            userRepository.deleteUserByEmail(email.toLowerCase());
            return ResponseEntity.accepted()
                    .body(MessageFormat.format("User with the email {0} deleted succesfully!" ,email));
        }
        return ResponseEntity.badRequest()
                .body(MessageFormat.format("User with the email {0} not found" ,email));
    }

    public ResponseEntity<String> updateUserEmail(String oldmail , String newmail){
        if(userRepository.count() == 0){
            return ResponseEntity.noContent().build();
        }
        if(userRepository.findUserByEmail(oldmail.toLowerCase()).isPresent()){
            userRepository.updateUserEmail(oldmail.toLowerCase(),newmail.toLowerCase());
            return ResponseEntity.accepted()
                    .body("Email changed succesfully!");
        }

        return ResponseEntity.badRequest()
                .body(MessageFormat.format("User with the email {0} was not found or there was a typo!" ,oldmail));
    }




   public ResponseEntity<String> addProductToUserBasket(Long uId , String productName){
        if(userRepository.count() == 0 || productRepository.count() == 0){
            return ResponseEntity.noContent().build();
        }
        if(userRepository.findById(uId).isPresent() && productRepository.findByName(productName).isPresent()){
            userRepository.findById(uId).get().getBasket().add(productRepository.findByName(productName).get());
            return ResponseEntity.ok("Added Succesfully");
        }
        return ResponseEntity.badRequest().body("Something went wrong");


    }

    public ResponseEntity<String> removeProductFromUserBasket(Long userID , Long productId){
        if(!userRepository.findById(userID).isPresent()){
            return ResponseEntity.badRequest().body("User not found!");
        }

        if(!userRepository.findById(userID).get().getBasket().contains(productRepository.findById(productId)) || userRepository.findById(userID).get().getBasket().isEmpty()) {
            return ResponseEntity.badRequest().body("Basket is empty or the product was not found!");

        }
        userRepository.findById(userID).get().getBasket().remove(productRepository.findById(productId).get());
        return ResponseEntity.ok("Product Deleted from the basket");
    }

    public ResponseEntity<User> getUserByEmail(String email){
        if(!userRepository.findUserByEmail(email).isPresent()){
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<User>(userRepository.findUserByEmail(email).get() , HttpStatus.OK);
    }

}
