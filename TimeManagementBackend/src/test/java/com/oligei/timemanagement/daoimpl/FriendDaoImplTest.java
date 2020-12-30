package com.oligei.timemanagement.daoimpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.oligei.timemanagement.dao.FriendDao;
import com.oligei.timemanagement.dto.FriendAlarmMsg;
import com.oligei.timemanagement.entity.AskNeo4j;
import com.oligei.timemanagement.entity.FollowNeo4j;
import com.oligei.timemanagement.entity.SetNeo4j;
import com.oligei.timemanagement.entity.UserNeo4j;
import com.oligei.timemanagement.factories.AskNeo4jFactory;
import com.oligei.timemanagement.factories.FollowNeo4jFactory;
import com.oligei.timemanagement.factories.SetNeo4jFactory;
import com.oligei.timemanagement.factories.UserNeo4jFactory;
import com.oligei.timemanagement.repository.AskNeo4jRepository;
import com.oligei.timemanagement.repository.FollowNeo4jRepository;
import com.oligei.timemanagement.repository.SetNeo4jRepository;
import com.oligei.timemanagement.repository.UserNeo4jRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class FriendDaoImplTest {

    @Autowired
    private FriendDao friendDao;

    @MockBean
    private UserNeo4jRepository userNeo4jRepository;

    @MockBean
    private FollowNeo4jRepository followNeo4jRepository;

    @MockBean
    private AskNeo4jRepository askNeo4jRepository;

    @MockBean
    private SetNeo4jRepository setNeo4jRepository;

    private static List<UserNeo4j> userNeo4jPool = new ArrayList<>();
    private static List<FollowNeo4j> followNeo4jPool = new ArrayList<>();
    private static List<AskNeo4j> askNeo4jPool = new ArrayList<>();
    private static List<SetNeo4j> setNeo4jPool = new ArrayList<>();

    @BeforeAll
    static void buildInstance() {
        for (int i = 0; i <= 5; i++) {
            userNeo4jPool.add(UserNeo4jFactory.buildUserNeo4jByUserId(Integer.toString(i)));
        }
        followNeo4jPool.add(FollowNeo4jFactory.buildFollowNeo4jByNode(userNeo4jPool.get(1), userNeo4jPool.get(2)));
        followNeo4jPool.add(FollowNeo4jFactory.buildFollowNeo4jByNode(userNeo4jPool.get(2), userNeo4jPool.get(1)));
        followNeo4jPool.add(FollowNeo4jFactory.buildFollowNeo4jByNode(userNeo4jPool.get(1), userNeo4jPool.get(3)));
        followNeo4jPool.add(FollowNeo4jFactory.buildFollowNeo4jByNode(userNeo4jPool.get(3), userNeo4jPool.get(1)));
        askNeo4jPool.add(AskNeo4jFactory.buildAskNeo4jByNode(userNeo4jPool.get(1), userNeo4jPool.get(4)));
        askNeo4jPool.add(AskNeo4jFactory.buildAskNeo4jByNode(userNeo4jPool.get(1), userNeo4jPool.get(5)));
        setNeo4jPool.add(SetNeo4jFactory.buildSetNeo4jByNode(userNeo4jPool.get(4), userNeo4jPool.get(5)));
    }

    @Test
    void getFriendsList() {
        int userId = 1;
        List<UserNeo4j> friends = new ArrayList<>();
        friends.add(userNeo4jPool.get(2));
        friends.add(userNeo4jPool.get(3));
        when(userNeo4jRepository.getFriendsList(Integer.toString(userId))).thenReturn(friends);
        assertThrows(NullPointerException.class, () -> friendDao.getFriendsList(null));
        assertEquals(2, friendDao.getFriendsList(userId).size());
    }

    @Test
    void getFollowRelation() {
        int from = 1, to = 2;
        FollowNeo4j followNeo4j = followNeo4jPool.get(0);
        when(followNeo4jRepository.getFollowRelation(Integer.toString(from), Integer.toString(to)))
                .thenReturn(followNeo4j);
        assertThrows(NullPointerException.class, () -> friendDao.getFollowRelation(null, null));
        assertEquals(to, Integer.valueOf(friendDao.getFollowRelation(from, to).getFriend().getUserId()));
    }

    @Test
    void getAskRelation() {
        int from = 1, to = 4;
        AskNeo4j askNeo4j = askNeo4jPool.get(0);
        when(askNeo4jRepository.getAskRelation(Integer.toString(from), Integer.toString(to))).thenReturn(askNeo4j);
        assertThrows(NullPointerException.class, () -> friendDao.getAskRelation(null, null));
        assertEquals(to, Integer.valueOf(friendDao.getAskRelation(from, to).getFriend().getUserId()));
    }

    @Test
    void addAskRelation() {
        int from = 2, to = 4;
        when(askNeo4jRepository.addAskRelation(Integer.toString(from), Integer.toString(to)))
                .thenReturn(new AskNeo4j());
        assertThrows(NullPointerException.class, () -> friendDao.addAskRelation(null, null));
        assertNotNull(friendDao.addAskRelation(from, to));
        verify(askNeo4jRepository).addAskRelation(Integer.toString(from), Integer.toString(to));
    }

    @Test
    void getFriendRequest() {
        int userId = 4;
        List<UserNeo4j> userNeo4js = new ArrayList<>();
        userNeo4js.add(userNeo4jPool.get(1));
        when(userNeo4jRepository.getFriendRequest(Integer.toString(userId))).thenReturn(userNeo4js);
        assertThrows(NullPointerException.class, () -> friendDao.getFriendRequest(null));
        assertEquals(1, friendDao.getFriendRequest(userId).size());
    }

    @Test
    void deleteAskRelation() {
        int from = 1, to = 4;
        doNothing().when(askNeo4jRepository).deleteAskRelation(Integer.toString(from), Integer.toString(to));
        assertThrows(NullPointerException.class, () -> friendDao.deleteAskRelation(null, null));
        friendDao.deleteAskRelation(from, to);
        verify(askNeo4jRepository).deleteAskRelation(Integer.toString(from), Integer.toString(to));
    }

    @Test
    void addFollowRelation() {
        int from = 2, to = 4;
        when(followNeo4jRepository.addFollowRelation(Integer.toString(from), Integer.toString(to)))
                .thenReturn(new FollowNeo4j());
        assertThrows(NullPointerException.class, () -> friendDao.addFollowRelation(null, null));
        assertNotNull(friendDao.addFollowRelation(from, to));
        verify(followNeo4jRepository).addFollowRelation(Integer.toString(from), Integer.toString(to));
    }

    @Test
    void saveAlarmForFriend() {
        int from = 1, to = 2;
        FriendAlarmMsg friendAlarmMsg = new FriendAlarmMsg("test1", "clock");
        when(setNeo4jRepository
                     .addSetRelation(Integer.toString(from), Integer.toString(to), friendAlarmMsg.getUsername()
                             , friendAlarmMsg.getClockSetting()))
                .thenReturn(new SetNeo4j());
        assertThrows(NullPointerException.class, () -> friendDao.saveAlarmForFriend(null, null, null));
        assertNotNull(friendDao.saveAlarmForFriend(from, to, friendAlarmMsg));
        verify(setNeo4jRepository)
                .addSetRelation(Integer.toString(from), Integer.toString(to), friendAlarmMsg.getUsername()
                        , friendAlarmMsg.getClockSetting());
    }

    @Test
    void getAlarmRequest() {
        int userId = 5;
        when(setNeo4jRepository.getAlarmRequest(Integer.toString(userId))).thenReturn(setNeo4jPool);
        assertThrows(NullPointerException.class, () -> friendDao.getAlarmRequest(null));
        assertEquals(1, friendDao.getAlarmRequest(userId).size());
    }
}
