package com.hanium.memotion.service;


import com.hanium.memotion.domain.Member;
import com.hanium.memotion.exception.base.BaseException;
import com.hanium.memotion.exception.base.ErrorCode;
import com.hanium.memotion.exception.custom.BadRequestException;
import com.hanium.memotion.repository.MemberRepository;
import com.hanium.memotion.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.Random;


@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class MailService {
    private final JavaMailSender javaMailSender;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtService;
    private final PasswordEncoder passwordEncoder;

    @Value("${spring.mail.username}")
    private String email;

    // 비밀번호 변경 이메일 내용
    private MimeMessage createPasswordMessage(String code, String emailTo) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, emailTo); //보내는 사람
        message.setSubject("Memotion 이메일로 임시 비밀번호 찾기 :"); //메일 제목
        message.setText(code, "utf-8", "html"); //내용, charset타입, subtype
        message.setFrom(new InternetAddress(email,"Memotion_Official")); //보내는 사람의 메일 주소, 보내는 사람 이름

        log.info("message : " + message);
        return message;
    }

    // 임시 비밀번호 생성 (11자리로 생성)
    private String createPassword() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        log.info("임시 비밀번호 : " + generatedString);
        return generatedString + "!";
    }

    // 메일로 임시 비밀번호 발송
    public String sendPasswordMail(String email) throws BaseException {
        //이메일 중복 확인
        Optional<Member> getMember = memberRepository.findByEmail(email);
        if(getMember.isEmpty())
            throw new BadRequestException("해당하는 회원을 찾을 수 없습니다.");

        String password = createPassword();
        try{
            // 1. DB 임시 비밀번호로 변경
            Member member = getMember.get();
            member.updatePassword(passwordEncoder.encode(password));
            memberRepository.save(member);

            // 2. 임시 비밀번호 메일로 보내주기
            MimeMessage mimeMessage = createPasswordMessage(password, email);
            javaMailSender.send(mimeMessage);
            return password;
        } catch (Exception e){
            e.printStackTrace();
            throw new BaseException(ErrorCode.EMAIL_SEND_ERROR);
        }
    }

    // 인증 코드 이메일 내용
    private MimeMessage createCodeMessage(String code, String emailTo) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, emailTo); //보내는 사람
        message.setSubject("Memotion 이메일 인증번호 :"); //메일 제목
        message.setText(code, "utf-8", "html"); //내용, charset타입, subtype
        message.setFrom(new InternetAddress(email,"Memotion_Official")); //보내는 사람의 메일 주소, 보내는 사람 이름

        log.info("message : " + message);
        return message;
    }

    // 인증 코드 생성 (5자리로 생성)
    private String createCode() {
        StringBuffer code = new StringBuffer();
        Random random = new Random();

        for(int i=0; i<5; i++) {
            code.append((random.nextInt(10)));
        }

        return code.toString();
    }

    // 메일로 인증번호 발송
    public String sendCodeMail(String email) throws BaseException {
        //이메일 중복 확인
        Optional<Member> getMember = memberRepository.findByEmail(email);
        if(getMember.isEmpty())
            throw new BadRequestException("해당하는 회원을 찾을 수 없습니다.");

        String code = createCode();
        try{
            MimeMessage mimeMessage = createPasswordMessage(code, email);
            javaMailSender.send(mimeMessage);
            return code;
        } catch (Exception e){
            e.printStackTrace();
            throw new BaseException(ErrorCode.EMAIL_SEND_ERROR);
        }
    }
}
