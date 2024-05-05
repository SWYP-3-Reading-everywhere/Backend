package com.book_everywhere.domain.pin.service;

import com.book_everywhere.common.exception.customs.CustomErrorCode;
import com.book_everywhere.common.exception.customs.EntityNotFoundException;
import com.book_everywhere.domain.auth.entity.User;
import com.book_everywhere.domain.auth.repository.UserRepository;
import com.book_everywhere.domain.pin.entity.Pin;
import com.book_everywhere.domain.pin.entity.Visit;
import com.book_everywhere.domain.pin.repository.PinRepository;
import com.book_everywhere.domain.pin.repository.VisitRepository;
import com.book_everywhere.domain.review.dto.ReviewRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class VisitServiceImpl implements VisitService{
    private final UserRepository userRepository;
    private final PinRepository pinRepository;
    private final VisitRepository visitRepository;

    @Transactional
    public void 독후감쓰기전방문등록또는수정(ReviewRespDto reviewRespDto) {
        //review가 올라가기전 visit에 등록되어있는지 확인후 없다면 visit등록
        User user = userRepository.findBySocialId(reviewRespDto.getSocialId()).orElseThrow(
                () -> new EntityNotFoundException(CustomErrorCode.PIN_NOT_FOUND)
        );
        Pin pin = pinRepository.mFindPinByAddress(reviewRespDto.getPinRespDto().getAddress());

        Visit visited = visitRepository.mFindByUserAndPin(user.getId(), pin.getId());

        if (visited == null) {
            Visit visit = Visit.builder()
                    .user(user)
                    .pin(pin)
                    .isPinPrivate(reviewRespDto.getPinRespDto().isPrivate())
                    .build();

            visitRepository.save(visit);
        } else {
            visited.changeVisit(user, pin, reviewRespDto.getPinRespDto().isPrivate());
        }
    }
}
