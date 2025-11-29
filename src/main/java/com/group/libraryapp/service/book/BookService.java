package com.group.libraryapp.service.book;

import com.group.libraryapp.domain.book.Book;
import com.group.libraryapp.domain.book.BookRepository;
import com.group.libraryapp.domain.user.User;
import com.group.libraryapp.domain.user.UserRepository;
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory;
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository;
import com.group.libraryapp.dto.book.request.BookLoanRequest;
import com.group.libraryapp.dto.book.request.BookReturnRequest;
import com.group.libraryapp.dto.book.request.BookCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final UserLoanHistoryRepository userLoanHistoryRepository;
    private final UserRepository userRepository;

    public BookService(BookRepository bookRepository, UserLoanHistoryRepository userLoanHistoryRepository
    , UserRepository userRepository) {
       this.bookRepository = bookRepository;
       this.userLoanHistoryRepository = userLoanHistoryRepository;
       this.userRepository = userRepository;
    }



    @Transactional
    public void saveBook(BookCreateRequest request) {
        bookRepository.save(new Book(request.getName()));
    }

    @Transactional
    public void loanBook(BookLoanRequest request) {
        // 1. 책 정보를 가져온다
        Book book = bookRepository.findByName(request.getBookName())
                .orElseThrow(IllegalArgumentException::new);

        // 2. 대출기록 정보를 확인해서 대출중인지 확인
        // 데이터가 있으면 true -> 대출중
        // 3. 만약에 확인했는데 대충 중 이라면 예외를 발생시킨다.
        if(userLoanHistoryRepository.existsByBookNameAndIsReturn(book.getName(), false)) {
            throw new IllegalArgumentException("진작 대출되어 있는 책입니다");
        }
        System.out.println("book : " + book);

        // 4. 유저 정보를 가져온다
        User user = userRepository.findByName(request.getUserName())
                .orElseThrow(IllegalArgumentException::new);
        System.out.println(user);
        user.loanBook(book.getName());      // user에 추가한 loanBook을 사용
        // 아래는 (5번) 사용하지않음

        // 5. 유저 정보와 책 정보를 기반으로 UserLoanHistory를 저장
        // userLoanHistoryRepository.save(new UserLoanHistory(user, book.getName()));
    }

    @Transactional
    public void returnBook(BookReturnRequest request) {
        // 유저 정보를 가져온다 userId 가 필요함
        User user = userRepository.findByName(request.getUserName())
                .orElseThrow(IllegalArgumentException::new);

        System.out.println("hello");

        // 객체지향적으로 -> user 안에 returnBook 사용
        user.returnBook(request.getBookName());

        // 유저 아이디, 책이름으로 대출 기록을 찾는다
       /* UserLoanHistory history = userLoanHistoryRepository.findByUserIdAndBookName(user.getId(), request.getBookName())
                .orElseThrow(IllegalArgumentException::new);
        // 반납 처리를 해준다
        history.doReturn();*/

        // 반납 처리 업데이트 저장
        // userLoanHistoryRepository.save(history); // Transactional 영속성 컨텍스트이 존재하고 영속석 컨텍스트는 변경감지 기능이 있기 때문에 save 를 해주지 않아도 자동으로 해준다


    }


}
