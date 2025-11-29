package com.group.libraryapp.domain.user;

import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = null;

    @Column(nullable = false, length = 20, name = "name")
    private String name;

    // table 의 컬럼과 같으면 어노테이션 작성 안해줘도 됨
    private Integer age;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)  // 연관관계 주인을 설정해줘야함
    private List<UserLoanHistory> userLoanHistories = new ArrayList<>();

    protected User() {}  // jpa 를 사용하려면 기본생성자가 꼭 필요하다

    public User(String name, Integer age) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(String.format("잘못된 name(%s)이 들어왔습니다", name));
        }
        this.name=name;
        this.age = age;


    }
    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public Long getId() {
        return id;
    }

    public void updateName(String name) {
        this.name = name;
    }

    // 객체지향적으로 개발 user안에 UserLoanHistory 사용 -> 도메인 계층에 비즈니스 로직이 들어갔다고 표현.
    public void loanBook(String bookName) {
        System.out.println(bookName);
        this.userLoanHistories.add(new UserLoanHistory(this, bookName));
    }

    // 반납 // user가 본인과 연결되어있는 책 대출중에서 책이름 기록을 가지고 있는 히스토리를 찾아서 반납처리.
    public void returnBook(String bookName) {
        UserLoanHistory targetHistory = this.userLoanHistories.stream()
                .filter(history -> history.getBookName().equals(bookName))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
        targetHistory.doReturn();
    }
}
