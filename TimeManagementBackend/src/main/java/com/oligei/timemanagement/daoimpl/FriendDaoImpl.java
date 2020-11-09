package com.oligei.timemanagement.daoimpl;

import com.oligei.timemanagement.dao.FriendDao;
import com.oligei.timemanagement.entity.AskNeo4j;
import com.oligei.timemanagement.entity.FollowNeo4j;
import com.oligei.timemanagement.entity.UserNeo4j;
import com.oligei.timemanagement.repository.AskNeo4jRepository;
import com.oligei.timemanagement.repository.FollowNeo4jRepository;
import com.oligei.timemanagement.repository.UserNeo4jRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class FriendDaoImpl implements FriendDao {

    @Autowired
    private UserNeo4jRepository userNeo4jRepository;

    @Autowired
    private FollowNeo4jRepository followNeo4jRepository;

    @Autowired
    private AskNeo4jRepository askNeo4jRepository;

    @Override
    public List<UserNeo4j> getFriendsList(Integer userId) {
        Objects.requireNonNull(userId, "null userId --FriendDaoImpl getFriendsList");
        return userNeo4jRepository.getFriendsList(userId.toString());
    }

    @Override
    public FollowNeo4j getFollowRelation(Integer from, Integer to) {
        Objects.requireNonNull(from, "null from --FriendDaoImpl getFollowRelation");
        Objects.requireNonNull(to, "null to --FriendDaoImpl getFollowRelation");
        return followNeo4jRepository.getFollowRelation(from.toString(), to.toString());
    }

    @Override
    public AskNeo4j getAskRelation(Integer from, Integer to) {
        Objects.requireNonNull(from, "null from --FriendDaoImpl getAskRelation");
        Objects.requireNonNull(to, "null to --FriendDaoImpl getAskRelation");
        return askNeo4jRepository.getAskRelation(from.toString(), to.toString());
    }

    @Override
    public AskNeo4j addAskRelation(Integer from, Integer to) {
        Objects.requireNonNull(from, "null from --FriendDaoImpl addAskRelation");
        Objects.requireNonNull(to, "null to --FriendDaoImpl addAskRelation");
        return askNeo4jRepository.addAskRelation(from.toString(), to.toString());
    }

    @Override
    public List<UserNeo4j> getFriendRequest(Integer userId) {
        Objects.requireNonNull(userId, "null userId --FriendDaoImpl getFriendRequest");
        return userNeo4jRepository.getFriendRequest(userId.toString());
    }

    @Override
    public void deleteAskRelation(Integer from, Integer to) {
        Objects.requireNonNull(from, "null from --FriendDaoImpl deleteAskRelation");
        Objects.requireNonNull(to, "null to --FriendDaoImpl deleteAskRelation");
        askNeo4jRepository.deleteAskRelation(from.toString(), to.toString());
    }

    @Override
    public FollowNeo4j addFollowRelation(Integer from, Integer to) {
        Objects.requireNonNull(from, "null from --FriendDaoImpl addFollowRelation");
        Objects.requireNonNull(to, "null to --FriendDaoImpl addFollowRelation");
        return followNeo4jRepository.addFollowRelation(from.toString(), to.toString());
    }
}
