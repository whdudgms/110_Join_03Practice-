package com.feb.jdbc.service;

import java.util.HashMap;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.feb.jdbc.dao.MemberDao;
import com.feb.jdbc.entity.Member;
import com.feb.jdbc.util.Sha512Encoder;

@Service
public class MemberService {
	
	@Autowired
	MemberDao memberDao;
	
	/**
	 * 
	 * @param params member가입 데이터정보.
	 * @return 중복체크, memberId 글자수 체크를 먼저하고 가입을 진행함.
	 */
	public boolean join(HashMap<String,String> params) {
		System.out.println("출");
		System.out.println(params);
		System.out.println("출");
		Member membmer = memberDao.selectMember(params.get("memberId"));
		if(Objects.nonNull(membmer)| params.get("memberId").length() < 7)
			return false;
		params.put("memberName", params.get("memberId"));
		Sha512Encoder encoder = Sha512Encoder.getInstance();
		params.put("passwd",encoder.getSecurePassword(params.get("passwd")));
		return memberDao.insertMember(params)==1;
	}
	
	/**
	 * 
	 * @param param 로그인 정보 
	 * @return 비밀번호 암호화해서 비교.
	 */
	public boolean login(HashMap<String,String > params) {
		Member membmer = memberDao.selectMember(params.get("memberId"));
		if(Objects.isNull(membmer))
			return false;
		String equPw =membmer.getPasswd();
		String inputPw = Sha512Encoder.getInstance().getSecurePassword(params.get("passwd"));
		return equPw.equals(inputPw);
	}

}
