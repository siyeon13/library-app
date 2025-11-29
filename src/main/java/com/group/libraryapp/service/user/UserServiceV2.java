package com.group.libraryapp.service.user;

import com.group.libraryapp.domain.user.User;
import com.group.libraryapp.domain.user.UserRepository;
import com.group.libraryapp.dto.user.request.UserCreateRequest;
import com.group.libraryapp.dto.user.request.UserUpdateRequest;
import com.group.libraryapp.dto.user.response.UserResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceV2 {

    private final UserRepository userRepository;


    public UserServiceV2(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 아래 있는 함수가 시작될 때 start transaction; 을 해준다 (트랜잭션 시작)
    // 함수가 예외 없이 잘 끝났으면 commit
    // 혹시라도 문제가 있다면 rollback
    @Transactional
    public void saveUser(UserCreateRequest request) {
        /*User u = */
        userRepository.save(new User(request.getName(), request.getAge()));
       // u.getId();
    }

    // 유저 조회기능 findAll
    @Transactional(readOnly = true)
    public List<UserResponse> getUsers() {
        /*List<User> users = userRepository.findAll();
        return users.stream().map(user -> new UserResponse(user.getId(), user.getName(), user.getAge()))
                .collect(Collectors.toList());*/

        // 코드 깔끔하게 작성하는 법
        return userRepository.findAll().stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    // 유저 업데이트 기능
    @Transactional
    public void updateUser(UserUpdateRequest request) {
        // select * from user where id = ?
        // Optional<User>
       User user = userRepository.findById(request.getId())
                .orElseThrow(IllegalArgumentException::new);

       user.updateName(request.getName());
       userRepository.save(user);
    }

    // 삭제 기능
    @Transactional
    public void deleteUser(String name) {
        // SELECT * FROM USER WHERE NAME = ?
        User user = userRepository.findByName(name)
                .orElseThrow(IllegalArgumentException::new);
        if(user == null) {
            throw new IllegalArgumentException();
        }

        userRepository.delete(user);
    }


}
// spring data jpa 를 사용하는 것

// 서비스 계층의 역할 - 트랜잭션
// 트랜잭션 : 쪼갤 수 없는 업무의 최소 단위 -> 한번에 성공하거나, 하나라도 실패하면 다 실패하게끔 만든다. 각각 업무를 다 묶어서 쪼갤 수 없게 만듦
// 트랜잭션 시작 : start transaction;
// commit 성공  , rollback 실패
// ex ) 여러 sql 작업을 트랜잭션을 실행후에 쿼리를 실행하고 나서 commit 을 해야 성공이므로 그때서야 쿼리가 실행되어 디비에 추가된다.

// 주의사항 있음
// 영속성 컨텍스트 4가지 기능
