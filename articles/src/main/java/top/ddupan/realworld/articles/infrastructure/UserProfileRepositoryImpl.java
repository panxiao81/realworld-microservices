package top.ddupan.realworld.articles.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClient;
import top.ddupan.realworld.articles.domain.user.UserProfileDto;
import top.ddupan.realworld.articles.domain.user.UserProfileRepository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserProfileRepositoryImpl implements UserProfileRepository {
    private final RestClient restClient;

    @Override
    public Optional<UserProfileDto> findById(UUID id) {
        ResponseEntity<UserProfileDto> entity = restClient.get()
                .uri("/api/profiles/id/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(UserProfileDto.class);

        return Optional.ofNullable(entity.getBody());
    }
}
