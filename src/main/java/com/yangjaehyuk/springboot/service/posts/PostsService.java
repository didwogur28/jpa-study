package com.yangjaehyuk.springboot.service.posts;

import com.yangjaehyuk.springboot.domain.posts.Posts;
import com.yangjaehyuk.springboot.domain.posts.PostsRepository;
import com.yangjaehyuk.springboot.web.dto.PostsListResponseDto;
import com.yangjaehyuk.springboot.web.dto.PostsResponseDto;
import com.yangjaehyuk.springboot.web.dto.PostsSaveRequestDto;
import com.yangjaehyuk.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    @Transactional(readOnly = true)     // readOnly = true : 트랜잭션 범위는 유지하되, 조회 기능만 남겨두어 조회 속도가 개선되기 때문에 등록, 수정, 삭제 기능이 없는 서비스 메소드에 사용
    public Object findAllDesc() {

        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)     // = .map(posts -> new PostsListResponseDto(posts))
                .collect(Collectors.toList());
    }

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {

        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {

        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id));

        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }


    @Transactional
    public void delete(Long id) {

        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id));

        postsRepository.delete(posts);
    }

    public PostsResponseDto findById (Long id) {

        Posts entity = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id));

        return new PostsResponseDto(entity);
    }
}
