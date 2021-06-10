<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="user.UserDAO" %> 
<%@ page import="java.io.PrintWriter" %> <!-- JS문장을 작성하기 위해 사용 -->
<% request.setCharacterEncoding("UTF-8"); %> <!--  건너오는 모든 data를 UTF-8으로 받을 수 있도록함 -->
<jsp:useBean id ="user" class="user.User" scope="page"/> <!--js bean 1명의 user정보를 담을 수 있는 bean, scope="page"현재 페이지에서만 사용도리 수 있게함-->
<jsp:setProperty name="user" property="userID"/>
<jsp:setProperty name="user" property="userPassword"/>  
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JSP 게시판 웹 사이트</title>
</head>
<body>
	<%
		String userID = null;
		if (session.getAttribute("userID") != null){ //UserID로 세션이 존재하는 호원은
			userID = (String) session.getAttribute("userID"); //UserID에 세션값을 넣어준다. joinAction.jsp도 동일.
		}
		if (userID != null) {
			PrintWriter script = response.getWriter(); 
			script.println("<script>"); 
			script.println("alert('이미 로그인이 되어있습니다.')"); 
			script.println("location.href = 'main.jsp'");
			script.println("</script>");
		}
		
		UserDAO userDAO = new UserDAO(); //객체 생성
		//login.jsp에서 user ID, PW가 각각 입력이 되면 넘어와서 login함수에 넣어서 실행
		int result = userDAO.login(user.getUserID(), user.getUserPassword());
		//로그인 성공
		if (result == 1) { 
			session.setAttribute("userID", user.getUserID()); // session 값으로 getUserID, 해당회원의 아이디 값을 준다. 로그인을 성공한 회원에게 세션을 부여한다.
			PrintWriter script = response.getWriter(); //script 문장
			script.println("<script>"); //script 문장 실행
			script.println("location.href = 'main.jsp'"); //로그인 성공시 main.jps로 이동
			script.println("</script>");
		}
		//로그인 싪패
		else if (result == 0) { 
			PrintWriter script = response.getWriter(); 
			script.println("<script>"); 
			script.println("alert('비밀번호가 틀립니다.')"); 
			script.println("history.back()"); //이전 page로 돌려보냄
			script.println("</script>");
		}
		//일치하는 아이디 없을때
		else if (result == -1) { 
			PrintWriter script = response.getWriter(); 
			script.println("<script>"); 
			script.println("alert('존재하지 않는 아이디 입니다.')"); 
			script.println("history.back()"); 
			script.println("</script>");
		}
		//DB 오류
		else if (result == -2) { 
			PrintWriter script = response.getWriter(); 
			script.println("<script>"); 
			script.println("alert('데이터베이스 오류가 발생하였습니다.')"); 
			script.println("history.back()"); 
			script.println("</script>");
		}
	%>
</body>
</html>
