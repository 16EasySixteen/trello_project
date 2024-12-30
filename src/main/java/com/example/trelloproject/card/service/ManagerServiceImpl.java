package com.example.trelloproject.card.service;

import com.example.trelloproject.card.dto.addManager.AddManagerRequestDto;
import com.example.trelloproject.card.dto.addManager.AddManagerResponseDto;
import com.example.trelloproject.card.entity.Card;
import com.example.trelloproject.card.entity.Manager;
import com.example.trelloproject.card.repository.CardRepository;
import com.example.trelloproject.card.repository.ManagerRepository;
import com.example.trelloproject.user.entity.User;
import com.example.trelloproject.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class ManagerServiceImpl implements ManagerService {
    private final CardRepository cardRepository;
    private final ManagerRepository managerRepository;
    private final UserRepository userRepository;

    public ManagerServiceImpl(CardRepository cardRepository, ManagerRepository managerRepository, UserRepository userRepository) {
        this.cardRepository = cardRepository;
        this.managerRepository = managerRepository;
        this.userRepository = userRepository;
    }

    public AddManagerResponseDto createManager(Long cardId, AddManagerRequestDto addManagerRequestDto) {

        Card card = cardRepository.findById(cardId).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
        User user = userRepository.findById(addManagerRequestDto.getUserId()).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        Manager manager = new Manager(user, card);

        Manager savedManager = managerRepository.save(manager);

        return new AddManagerResponseDto(
                savedManager.getId(),
                savedManager.getManagerName(),
                savedManager.getCreatedAt(),
                savedManager.getUpdatedAt());
    }

    @Override
    @Transactional
    public void deleteManager(Long managerId) {

        Manager findManager = managerRepository.findById(managerId).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        managerRepository.deleteById(managerId);
    }


}
