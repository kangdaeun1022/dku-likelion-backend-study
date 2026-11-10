package com.ll.demo03;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class SurlService {
    private final SurlRepository surlRepository;
    private final MemberRepository memberRepository;

    public SurlService(SurlRepository surlRepository, MemberRepository memberRepository) {
        this.surlRepository = surlRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public SurlResponse add(String body, String url, long authorId) {
        if (body == null || body.isBlank() || url == null || url.isBlank()) {
            throw new GlobalException(400, "설명과 URL을 입력해 주세요.");
        }
        URI uri;
        try {
            uri = URI.create(url);
        } catch (IllegalArgumentException exception) {
            throw new GlobalException(400, "올바른 URL을 입력해 주세요.");
        }
        if (!"https".equalsIgnoreCase(uri.getScheme()) && !"http".equalsIgnoreCase(uri.getScheme())
                || uri.getHost() == null) {
            throw new GlobalException(400, "http 또는 https URL만 등록할 수 있습니다.");
        }
        Member author = memberRepository.getReferenceById(authorId); // 불필요한 SELECT 없이 FK 연결
        return SurlResponse.from(surlRepository.save(new Surl(body, url, author)));
    }

    public List<SurlResponse> findAll() {
        return surlRepository.findAll().stream().map(SurlResponse::from).toList();
    }

    @Transactional
    public String visit(long id) {
        Surl surl = surlRepository.findById(id)
                .orElseThrow(() -> new GlobalException(404, "단축 URL을 찾을 수 없습니다."));
        surl.increaseCount(); // 변경 감지로 count UPDATE
        return surl.getUrl();
    }
}
