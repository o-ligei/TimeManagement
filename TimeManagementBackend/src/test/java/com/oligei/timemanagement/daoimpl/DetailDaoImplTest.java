package com.oligei.timemanagement.daoimpl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.oligei.timemanagement.dao.DetailDao;
import com.oligei.timemanagement.entity.Detail;
import com.oligei.timemanagement.factories.DetailFactory;
import com.oligei.timemanagement.repository.DetailRepository;
import com.oligei.timemanagement.utils.FormatUtil;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class DetailDaoImplTest {

    @Autowired
    private DetailDao detailDao;

    @MockBean
    private DetailRepository detailRepository;

    private static List<Detail> detailPool = new ArrayList<>();

    @BeforeAll
    static void buildInstance() {
        for (int i = 0; i <= 5; i++) { detailPool.add(DetailFactory.buildDetailByUserId(i)); }
    }

    @Test
    void save() {
        int userId = 1;
        Detail detail = detailPool.get(userId);
        when(detailRepository.save(argThat(detailArg -> detailArg.getUserId() == userId))).thenReturn(detail);
        assertThrows(NullPointerException.class, () -> detailDao.save(null));
        assertNotNull(detailDao.save(detail));
        verify(detailRepository).save(argThat(detailArg -> detailArg.getUserId() == userId));
    }

    @Test
    void getDetailsByUserId() {
        int userId = 1;
        List<Detail> details = new ArrayList<>();
        details.add(detailPool.get(userId));
        when(detailRepository.getDetailsByUserId(userId)).thenReturn(details);
        assertThrows(NullPointerException.class, () -> detailDao.getDetailsByUserId(null));
        assertEquals(userId, detailDao.getDetailsByUserId(userId).get(0).getUserId());
    }

    @Test
    void getDetailsByUserIdAndTimestampGreaterThanEqual() {
        int userId = 1;
        Timestamp timestamp1 = FormatUtil.strToTimestamp("2020-09-18 20:00:00"),
                timestamp2 = FormatUtil.strToTimestamp("2077-12-12 12:00:00");
        List<Detail> details = new ArrayList<>(), emptyDetails = new ArrayList<>();
        details.add(detailPool.get(userId));
        when(detailRepository.getDetailsByUserIdAndTimestampGreaterThanEqual(userId, timestamp1)).thenReturn(details);
        when(detailRepository.getDetailsByUserIdAndTimestampGreaterThanEqual(userId, timestamp2))
                .thenReturn(emptyDetails);
        assertThrows(NullPointerException.class,
                     () -> detailDao.getDetailsByUserIdAndTimestampGreaterThanEqual(null, null));
        assertTrue(detailDao.getDetailsByUserIdAndTimestampGreaterThanEqual(userId, timestamp1).get(0).getTimestamp()
                            .after(timestamp1));
        assertEquals(0, detailDao.getDetailsByUserIdAndTimestampGreaterThanEqual(userId, timestamp2).size());
    }
}
