package com.group.libraryapp.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByName(String name);
    // 반환타임 User , 유저가 없다면 null 이 반환된다.
    // findByName : 함수 이름만 작성하면, 알아서 sql 이 조립된다!
    // find 라고 작성하면, 1개의 데이터만 가져온다.
    // By 뒤에 붙는 필드 이름으로 select 쿼리의 where 문이 작성된다.

    // boolean existByName(String name);  // 쿼리 결과가 존재하는 지 확인 반환타입은 boolean

    // count : 반환타입 long  : 결과 개수를 반환

    // By 뒤에 And 나 Or 로 조합할 수 도 있다
    // By 뒤에 GreaterThan : 초과
    // LessThan : 미만 ... 등 여러가지가 있음




}
