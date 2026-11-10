package com.ll.demo03;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Transactional
    public RsData<Article> write(String title, String body, Member author) {
        Article article = articleRepository.save(new Article(title, body, author));
        return RsData.success("게시물이 등록되었습니다.", article);
    }

    @Transactional
    public void seedIfEmpty(Member author) {
        if (articleRepository.count() == 0) {
            articleRepository.save(new Article("첫 게시물", "JPA INSERT 실습", author));
            articleRepository.save(new Article("두 번째 게시물", "재시작해도 남는 데이터", author));
        }
    }

    @Transactional
    public Article modify(long id, String title, String body) {
        Article article = getById(id);
        article.modify(title, body); // 트랜잭션 종료 시 변경 감지로 UPDATE
        return article;
    }

    @Transactional
    public void delete(long id) {
        articleRepository.delete(getById(id));
    }

    public long count() {
        return articleRepository.count();
    }

    public Article getById(long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new GlobalException(404, "게시물을 찾을 수 없습니다."));
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public List<Article> findByIds(Collection<Long> ids) {
        return articleRepository.findByIdInOrderByIdAsc(ids);
    }

    public List<Article> findByTitleContaining(String title) {
        return articleRepository.findByTitleContaining(title);
    }

    public List<Article> findByTitleAndBody(String title, String body) {
        return articleRepository.findByTitleAndBody(title, body);
    }
}
