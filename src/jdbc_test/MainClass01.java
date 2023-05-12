package jdbc_test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import common.DBConnection;

class MemberDTO{
	private int age;
	private String name, id;

	public MemberDTO() {}	//기본생성자

	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}

class DB{
	//데이터 베이스 연결 객체 (로그인)
	Connection con;

	//DB명령어 전송 객체
	PreparedStatement ps;

	//DB명령어 후 결과 얻어오는 객체
	ResultSet rs;
	
	public DB() {	//공통으로 만들어진 로그인파일을 가져와서 사용
		try {
			con = DBConnection.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
/*
	public DB() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("드라이브 로드 성공");
			//오라클 연결된 연결 객체를 얻어오는 기능
			String id = "C##java" , pwd = "1234";
			String url = "jdbc:oracle:thin:@localhost:1521/xe";

			con = DriverManager.getConnection(url, id, pwd);
			System.out.println("연결 성공");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
*/

	
	
	
	
	
	public void select() {
		String sql = "select * from newst";		//쿼리문 작성
		try {
			ps = con.prepareStatement(sql);		//select는 무조건 executeQuery를 사용한다.
			
			rs = ps.executeQuery();

			while(rs.next()) {//rs.next가 false가 되면 멈춤
				// next()를 이용해서 데이터가 있는지 확인
				// 데이터가 존재하면 true
				System.out.println(rs.getString("id"));
				System.out.println(rs.getString("name"));
				System.out.println(rs.getInt("age"));
			}

			/*
			System.out.println(rs.next());
			System.out.println(rs.getString("id"));
			System.out.println(rs.getString("name"));
			System.out.println(rs.getInt("age"));

			System.out.println(rs.next());
			System.out.println(rs.getString("id"));
			System.out.println(rs.getString("name"));
			System.out.println(rs.getInt("age"));
			System.out.println(rs.next());*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	
	
	//모든 사용자 보기
	
	public ArrayList<MemberDTO> select_2() {
		String sql = "select * from newst";
		ArrayList<MemberDTO> list = new ArrayList<>();
		try {
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while(rs.next()) {
				MemberDTO dto = new MemberDTO();
				//dto를 arraylist에 저장시킴
				dto.setId(rs.getString("id"));
				dto.setName(rs.getString("name"));
				dto.setAge(rs.getInt("age"));

				list.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	
	
	
	//검색기능
	
	public MemberDTO search(String id) {
		String sql = "select * from newst where id ='"+id+"'";
												//큰따옴표를 이용해서 id를 넣어준다.
		MemberDTO dto = null;	//값을 받아야 하니 dto를 일단 초기화 시켜준다.
		try {
			ps = con.prepareStatement(sql);	//쿼리문 실행
			rs = ps.executeQuery();
			if(rs.next()) {	//if문이 실행되면 객체가 있고 실행안되면 null 값
				dto = new MemberDTO();
				dto.setId(rs.getString("id"));
				dto.setName(rs.getString("name"));
				dto.setAge(rs.getInt("age"));
				/*
				System.out.println(rs.getString("id"));
				System.out.println(rs.getString("name"));
				System.out.println(rs.getInt("age"));
				*/
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return dto;
	}
	
	
	
	
	
	
	
	
	
	//회원가입 기능
	
	public int register(MemberDTO dd) {//매개변수 MemberDTO dd
		int result = 0;
		
		String sql =
				"insert into newst(id, name, age) values(?,?,?)";	//값에 ? 를 입력(나중에 추가하겠다)
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, dd.getId());		// ?에 들어가는 순서 차례대로 입력 
			ps.setString(2, dd.getName());
			ps.setInt(3, dd.getAge());
			
			//ps.executeQuery();
			// select : executeQuery사용
			// select를 제외한 나머지 : update 사용
			result = ps.executeUpdate();	//executeUpdate()는 숫자를 반환한다.
			
		}catch(Exception e) {
			//e.printStackTrace(); 주석처리를 하게되면 콘솔창이 깔끔해진다.
		}
		return result;
	}
	
	
	
	
	
	//회원 삭제
	public int delete(String id) {
		String sql = "delete from newst where id = ?";
		int result = 0;
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, id);
			result = ps.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	
}


public class MainClass01 {
	public static void main(String[] args) {
		DB db = new DB();
		//db.select();
		Scanner scan = new Scanner(System.in);
		int num, age = 0;
		String id = null, name = null;
		while(true) {
			System.out.println("1.모든 사용자 보기");
			System.out.println("2.검색");
			System.out.println("3.회원 가입");
			System.out.println("4.회원 삭제");
			num = scan.nextInt();
			switch(num) {
			case 1: 
				//리스트를 불러옴
				ArrayList<MemberDTO> list = db.select_2();
				System.out.println("id\tname\tage");
				System.out.println("=======================");
				
				//향상된 for문로 list에 저장된 데이터들을 명시해서 출력
				for(MemberDTO m : list) {
					System.out.print(m.getId() + "\t");
					System.out.print(m.getName() + "\t");
					System.out.println(m.getAge());
					System.out.println("=======================");
					
				}
				break;
				/*
				//일반 for문 작성시
				for(int i=0; i<list.size(); i++) {
					System.out.println(list.get(i).getId());
					System.out.println(list.get(i).getName());
					System.out.println(list.get(i).getAge());
				 */
			case 2:
				System.out.println("검색 id 입력");
				id = scan.next();
				MemberDTO d = db.search(id);	//id를 입력받아야 하니깐 id를 넣음
				if(d == null) {
					System.out.println("존재하지 않는 아이디!!!");
				}else {
					System.out.println("id: "+d.getId());
					System.out.println("name: "+d.getName());
					System.out.println("age: "+d.getAge());
				}
				break;
				
			case 3:
				System.out.println("아이디 입력");
				id = scan.next();
				System.out.println("이름 입력");
				name = scan.next();
				System.out.println("나이 입력");
				age = scan.nextInt();
				
				//사용자에게 입력을 받고 DTO에 저장 (변수이름 다름)
				MemberDTO dd = new MemberDTO();
				dd.setId(id);
				dd.setName(name);
				dd.setAge(age);
				
				int result = db.register(dd);
				if(result == 0) {
					System.out.println("동일한 아이디가 존재합니다.");
				}else {
					System.out.println("회원 가입을 축하합니다.");
				}
				
				db.register(dd);
				
				break;
				
			case 4:
				System.out.println("아이디 입력");
				id = scan.next();
				int result1 = db.delete(id);
				if(result1 == 0) {
					System.out.println("존재하지 않는 회원");
				}else {
					System.out.println(id + "님은 삭제 !!!");
				}
				break;
			}
			

		}
	}
}



















