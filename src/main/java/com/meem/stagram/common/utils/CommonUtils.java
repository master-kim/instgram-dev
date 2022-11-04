package com.meem.stagram.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import com.meem.stagram.file.FileEntity;
import com.meem.stagram.follow.FollowEntity;
import com.meem.stagram.follow.IFollowRepository;
import com.meem.stagram.post.IPostRepository;
import com.meem.stagram.post.PostEntity;
import com.meem.stagram.story.IStoryRepository;
import com.meem.stagram.user.IUserRepository;

import lombok.RequiredArgsConstructor;

/**
 * 설명 : CommonUtils.java   (공통 작업 utils 모아두기)
 * ------------------------------------------------------------- 
 * 작업일          작업자    작업내용
 * ------------------------------------------------------------- 
 * 2022.10.25    김요한    최초작성 
 * 2022.10.25    김요한    유저에 대한 팔로우인원 리스트 공통 함수 추가 
 * 2022.11.03    김요한    Repository 공통 선언
 * 2022.11.04    김요한    PostList,FileList 통합 및 공통 클래스 선언
 * 2022.11.04    김요한    followList Json Length 비교 Obj 값 비교로 변경
 * -------------------------------------------------------------
 */

@RequiredArgsConstructor
public class CommonUtils {
    
    public static IFollowRepository ifollowrepository;
    
    public static IUserRepository iuserrepository;
    
    public static IStoryRepository istoryrepository;
    
    public static IPostRepository ipostrepository;
    
    // 2022.10.25.김요한 - 유저에 대한 팔로우인원 리스트를 뽑는 함수
    public static List<String> followList(String i_str_user_id) throws Exception{
        
        List<FollowEntity> followerList = ifollowrepository.findByUserId(i_str_user_id);
        //List<FollowEntity> followerList = i_follow_repository.findByUserId(i_str_user_id);
        
        List<String> strList = new ArrayList<String>();
        
        try {
            
            // 4.팔로우 리스트 데이터를 JSONArray로 데이터를 뽑아 결과 값을 맞추어 작성
            JSONArray jsonArr = new JSONArray(followerList.get(0).followerList);
            
            if (0 < jsonArr.getJSONObject(0).length()) {
                for (int idx = 0 , listCnt = jsonArr.length(); idx < listCnt; idx++) {   // JSONArray 내 json 개수만큼 for문 동작
                    JSONObject jsonObject = jsonArr.getJSONObject(idx);                  // index번째 Json데이터를 가져옴
                    strList.add(jsonObject.getString("user_id").toString());             // user_id 값을 사용하여 팔로우 인원을 가져온다.
                }
            
            } else {;}
            
            // 5. 내가 올린 스토리, 게시글도 가져오기 위해 결과값에 추가
            strList.add(i_str_user_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        return strList;
    }

 // 2022.11.04.김요한 - postIdList 뽑는 함수
    public static List<String> postIdList(List<PostEntity> postList) {
        
        List<String> strList = new ArrayList<String>();
        
        for (int postIdx = 0 , listCnt = postList.size(); postIdx < listCnt; postIdx++) {  
            String postId = postList.get(postIdx).getPostId().toString();
            strList.add(postId);             
        }
        
        return strList;
    }
    
    // 2022.11.04.김요한 - 게시글 , 파일 리스트 뽑아오기
    public static List<HashMap<String, Object>> postListAndFileList(List<PostEntity> postList, List<FileEntity> fileList) {
        
        List<HashMap<String, Object>> resultList = new ArrayList<>();
        
        for (int postIdx = 0 , postMax = postList.size(); postIdx < postMax; postIdx++ ) {
             
             for (int fileIdx = 0 , fileMax = postList.size(); fileIdx < fileMax; fileIdx++ ) {
                 
                 String postId = postList.get(postIdx).getPostId().toString();
                 String commonId = fileList.get(fileIdx).getCommonId().toString();
                 
                 if (postId.equals(commonId)) {
                     HashMap<String, Object> resultMap = new HashMap<>();
                     resultMap.put("postList", postList.get(postIdx));
                     resultMap.put("fileList", fileList.get(fileIdx));
                     resultList.add(resultMap);
                 } else {;}
             }
        }
        return resultList;
        
    }
}