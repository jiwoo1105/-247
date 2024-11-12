import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

interface UserOperation                     // 인터페이스 개념
{
    String login() throws IOException;

    void signUp() throws IOException;

    void chargePoint(String id) throws IOException;

    void userView(String id) throws IOException;

    void rentedBookView();

    void totalUserView();

    void saveUserMapToFile(String fileName);

    void loadUserMapFromFile(String fileName);
}

class UserData implements Serializable {

    Map<String, List<String>> rentMap = new HashMap<>();
    private String name = "";
    private String id = "";
    private String hp = "";
    private String cardCompany;
    private String cardNum;
    private int balance = 0;

    public UserData(String id, String name, String hp, String cardCompany, String cardNum)   // 생성자 개념
    {
        this.name = name;
        this.id = id;
        this.hp = hp;
        this.cardCompany = cardCompany;
        this.cardNum = cardNum;
    }

    // getter setter 설정
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance += balance;
    }

    public String getCardCompany() {
        return cardCompany;
    }

    public void setCardCompany(String cardCompany) {
        this.cardCompany = cardCompany;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public Map<String, List<String>> getRentMap() {
        return rentMap;
    }

    public void setRentIndex(String title, List<String> index) {
        rentMap.put(title, index);
    }

    public List<String> getRentIndex(String title) {
        return rentMap.get(title);
    }

}

class UserController implements UserOperation   // 상속 개념
{
    public Map<String, UserData> userMap = new HashMap<>();
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    UserData ud;

    public UserController() {
        String fileName = "userData.ser";

        if (!Files.exists(Paths.get(fileName))) {
            userMap.put("admin", new UserData("admin", "관리자", "", "", ""));
            userMap.put("sim5213", new UserData("sim5213", "김정현", "010-2349", "현대카드", "1234-5678"));
        }
    }

    @Override            // 오버라이딩 개념
    public String login() throws IOException {
        System.out.println("\n============= 로그인 =============");
        System.out.print("아이디를 입력해주세요 → ");
        String id = br.readLine();
        System.out.println();
        return id;

    }

    @Override
    public void signUp() throws IOException {

        String signid;        //-- 반복문 do while 종료를 위한 변수 선언
        System.out.println("\n============ 회원가입 ============");
        while (true) {
            System.out.print("사용하실 아이디를 입력 해주세요 → ");
            signid = br.readLine();

            if (userMap.containsKey(signid))                // 입력한 값이 발견 된다면 아랫값 실행
                System.out.println("※ 입력하신 아이디는 중복된 아이디 입니다!");
            else break;
        }

        System.out.print("이름을 입력 해주세요 → ");
        String name = br.readLine();

        System.out.print("전화번호를 입력 해주세요 → ");
        String hp = br.readLine();

        System.out.print("카드사 입력 → ");
        String cardCompany = br.readLine();

        System.out.print("카드번호 입력 → ");
        String cardNum = br.readLine();


        System.out.printf("\n%s님 정상적으로 가입 되었습니다!\n\n", signid);

        userMap.put(signid, new UserData(name, signid, hp, cardCompany, cardNum));
    }

    @Override
    public void chargePoint(String id) throws IOException {
        ud = userMap.get(id);

        System.out.println("\n=================================");
        System.out.printf("현재 포인트 : %d\n", ud.getBalance());
        System.out.println("=================================");
        System.out.println("1) 1000");
        System.out.println("2) 5000");
        System.out.println("3) 10000");
        System.out.println("4) 50000");
        System.out.println("5) 직접입력");
        System.out.println("6) 뒤로가기");
        System.out.println("=================================");
        System.out.print(">> 원하시는 금액을 선택해주세요 → ");
        int n = Integer.parseInt(br.readLine());
        switch (n) {
            case 1:
                ud.setBalance(1000);
                break;
            case 2:
                ud.setBalance(5000);
                break;
            case 3:
                ud.setBalance(10000);
                break;
            case 4:
                ud.setBalance(50000);
                break;
            case 5:
                System.out.print(">> 직접입력 → ");
                int point =Integer.parseInt(br.readLine());
                ud.setBalance(point);
                break;
            default:
                System.out.println();
                break;
        }
        if (n >= 1 && n <= 5) {
            System.out.println("\n정상적으로 충전되었습니다!");
            System.out.printf("충전 후 포인트 : %d\n\n", ud.getBalance());
        }
    }

    public void userView(String id) throws IOException {
        ud = userMap.get(id);

        System.out.println("\n========== 사용자 정보 ============");
        System.out.println("아이디      : " + ud.getId());
        System.out.println("이름       : " + ud.getName());
        System.out.println("전화번호    : " + ud.getHp());
        System.out.println("카드사      : " + ud.getCardCompany());
        System.out.println("카드번호    : " + ud.getCardNum());
        System.out.println("현재 포인트 : " + ud.getBalance());
        System.out.print("내가 대여한 만화 : \n");
        rentedBookView();
        System.out.println("=================================");
        System.out.print(">> 원하시는 업무를 선택해주세요. ([1] 카드 정보 변경, [2] 뒤로가기) → ");
        int n = Integer.parseInt(br.readLine());
        if (n == 1) changeCardInfo(ud);
        else System.out.println();
    }

    private void changeCardInfo(UserData ud) {
        System.out.println("\n========= 현재 카드 정보===========");
        System.out.printf("카드사 :  %s\n카드번호 : %s\n", ud.getCardCompany(), ud.getCardNum());
        System.out.println("=================================");
        System.out.print("변경할 정보 입력 (카드사, 카드번호 ',' 로 구분) → ");
        Scanner sc = new Scanner(System.in);
        String str = sc.next();
        String[] cardInfo = str.split(",");
        ud.setCardCompany(cardInfo[0].trim());
        ud.setCardNum(cardInfo[1].trim());

        System.out.println("\n========= 변경된 카드 정보=========");
        System.out.printf("카드사 : %s\n카드번호 : %s\n", ud.getCardCompany(), ud.getCardNum());
        System.out.println("=================================");
        System.out.println("위 카드 정보로 변경 완료 되었습니다!\n");

    }

    @Override
    public void rentedBookView() {
        int n = 1;
        Map<String, List<String>> um = ud.getRentMap();
        for (String key : um.keySet()) {
            System.out.printf("%d. [제목] %s, [대여 화] ", n++, key);
            for (String value : um.get(key)) {
                System.out.printf("%s화 ", value);
            }
            System.out.println();
        }
    }

    @Override
    public void totalUserView() {

        System.out.println();
        for (String id : userMap.keySet()) {
            UserData ud = userMap.get(id);

            System.out.println("-------- 고객정보 --------");
            System.out.printf("이름 : %s\n", ud.getName());
            System.out.printf("아이디 : %s\n", ud.getId());
            System.out.printf("전화번호 : %s\n", ud.getHp());
            System.out.print("대여도서 : \n");
            int n = 1;
            Map<String, List<String>> um = ud.getRentMap();
            for (String key : um.keySet()) {
                System.out.printf("%d. [제목] %s, [대여 화] ", n++, key);
                for (String value : um.get(key)) {
                    System.out.printf("%s화 ", value);
                }
                System.out.println();
            }

            System.out.println("=========================");

        }

    }

    // UserMap 프로그램 종료 시 객체 직렬화 저장
    @Override
    public void saveUserMapToFile(String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(fileName)))) {
            oos.writeObject(userMap);
            System.out.println("User 데이터 파일 저장 : " + fileName);
        } catch (IOException e) {
            System.err.println("User 데이터 저장 중 오류 : " + e.getMessage());
        }
    }


    // UserMap 데이터 시작 시 객채 역직렬화 로딩
    @Override
    public void loadUserMapFromFile(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(fileName)))) {
            userMap = (Map<String, UserData>) ois.readObject();
            System.out.println("User 데이터 파일 로드 : " + fileName);
        } catch (NoSuchFileException | FileNotFoundException e) {
            System.err.println("User 데이터 파일이 없습니다. 최초 실행 시 없을 수 있습니다.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("User 데이터 로딩 중 오류" + e.getMessage());
            e.printStackTrace();
        }
    }

}

class ManagerData implements Serializable {
    private final List<String> rentInfo = new ArrayList<>();
    private final List<String> saleInfo = new ArrayList<>();
    private int rent = 0;
    private int sale = 0;

    public List<String> getRentInfo() {
        return rentInfo;
    }

    public void addRentInfo(String title, String money, String company, String id, String date, String time) {
        rentInfo.add(title);
        rentInfo.add(company);
        rentInfo.add(id);
        rentInfo.add(money);
        rentInfo.add(date);
        rentInfo.add(time);
    }

    public List<String> getSaleInfo() {
        return saleInfo;
    }

    public void addSaleInfo(String title, String money, String company, String id, String date, String time) {
        saleInfo.add(title);
        saleInfo.add(company);
        saleInfo.add(id);
        saleInfo.add(money);
        saleInfo.add(date);
        saleInfo.add(time);
    }

    public int getRent() {
        return rent;
    }

    public void setRent(int rental) {
        this.rent += rental;
    }

    public int getSale() {
        return sale;
    }

    public void setSale(int sale) {
        this.sale += sale;
    }
}


class ManagerController {
    // static으로 선언하여 하나의 인스턴스만 공유하게 함
    static ManagerData managerData;
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    // static 생성자
    static {
        managerData = new ManagerData();
        String fileName = "managerData.ser";

        // 파일이 있을때 로딩
        if (Files.exists(Paths.get(fileName))) {
            loadManagerDataFromFile(fileName);
        }
    }

    public static int HomeUI() throws IOException {
        int num;

        System.out.println("================================ ");
        System.out.println("     Welcome to ToonStore247     ");
        System.out.println("================================\n");
        System.out.println("      ████████████████████");
        System.out.println("           툰스토어247    ");
        System.out.println("      ████████████████████\n");
        System.out.println("============= HOME =============");
        System.out.println("1. 로그인");
        System.out.println("2. 회원가입");
        System.out.println("3. 종료");
        System.out.println("=================================");

        do {
            System.out.print(">> 원하시는 메뉴를 선택해주세요 → ");
            num = Integer.parseInt(br.readLine());
        } while (num < 1 || num > 3);

        return num;
    }

    public static int CustomerUI() throws IOException {
        int num;

        System.out.println("============== MAIN =============");
        System.out.println("1. 전체 목록");
        System.out.println("2. 대여 및 구매");
        System.out.println("3. 요금 충전");
        System.out.println("4. 내 정보");
        System.out.println("5. 로그아웃");
        System.out.println("6. 종료");
        System.out.println("=================================");

        do {
            System.out.print(">> 원하시는 메뉴를 선택해주세요 → ");
            num = Integer.parseInt(br.readLine());
        } while (num < 1 || num > 6);

        return num;
    }

    public static int ManagerUI() throws IOException {
        int num;

        System.out.println("=========== ADMIN =============");
        System.out.println("1.도서정보관리");
        System.out.println("2.도서재고관리");
        System.out.println("3.매출확인");
        System.out.println("4.로그아웃");
        System.out.println("5.종료");
        System.out.println("=================================");

        do {
            System.out.print(">> 원하시는 메뉴를 선택해주세요 → ");
            num = Integer.parseInt(br.readLine());
        } while (num < 1 || num > 5);

        return num;

    }

    public static void manageComicInfo() throws IOException {
        ComicController cc = new ComicController();

        while (true) {
            System.out.println("\n========= 도서 정보 관리 =========");
            System.out.println("1) 신간 추가");
            System.out.println("2) 만화 정보 변경");
            System.out.println("3) 뒤로가기");
            System.out.println("================================");
            System.out.print(">> 원하시는 업무를 선택해주세요 → ");
            int n = Integer.parseInt(br.readLine());
            if (n == 1) {
                cc.comicAdd();
            } else if (n == 2) {
                System.out.print("\n정보 변경을 원하시는 만화를 입력해주세요 → ");
                String title = br.readLine();
                if(cc.comicMap.containsKey(title)) {
                    ComicData cd = cc.comicMap.get(title);
                    changeComicInfo(cd);
                }
                else System.out.println("\n툰스토어247에 존재하지 않는 만화입니다!");
            } else {
                System.out.println();
                break;
            }
        }
    }

    public static void manageComicInventory() throws IOException {
        ComicController cc = new ComicController();
        UserController uc = new UserController();

        System.out.println("\n========= 도서재고관리 ==========");
        System.out.println("1) 대여관리");
        System.out.println("2) 판매관리");
        System.out.println("3) 뒤로가기");
        System.out.println("================================");
        System.out.print(">> 원하시는 업무를 선택해주세요 → ");
        int n = Integer.parseInt(br.readLine());

        if (n == 1) {
            uc.totalUserView();
            System.out.println("1) 도서반납");
            System.out.println("2) 뒤로가기");
            System.out.println("================================");
            System.out.print(">> 원하시는 업무를 선택해주세요 → ");

            if (Integer.parseInt(br.readLine()) == 1) {

                System.out.print(">> 반납할 사용자의 ID 를 입력하시오 → ");
                String id = br.readLine();
                Map<String, List<String>> map = uc.userMap.get(id).getRentMap();
                if(map.isEmpty()) {
                    System.out.println("\n사용자가 대여중인 만화가 존재하지 않습니다!\n");
                }
                else {
                    System.out.print(">> 반납할 만화를 입력해주세요 → ");
                    String title = br.readLine();
                    System.out.print(">> 반납할 화를 입력해 주세요 → ");
                    int index = Integer.parseInt(br.readLine());
                    String[] tmp = cc.comicMap.get(title).getRentedBookStatus();

                    UserData ud = uc.userMap.get(id);
                    Map<String, List<String>> um = ud.getRentMap();
                    List<String> rentStatus;

                    if (ud.getRentIndex(title) != null) {
                        rentStatus = ud.getRentIndex(title);
                    } else rentStatus = new ArrayList<>();

                    tmp[index - 1] = "○";

                    rentStatus.remove(Integer.toString(index));

                    if (rentStatus.isEmpty()) um.remove(title);
                    else uc.userMap.get(id).setRentIndex(title, rentStatus);

                    System.out.printf("%s님의 대여 만화 : ", uc.userMap.get(id).getId());

                    if (uc.userMap.get(id).getRentIndex(title).isEmpty()) {
                        System.out.println(" - ");
                    } else {
                        for (String name : um.keySet())
                            System.out.printf("%s, ", name);
                        for (String idx : uc.userMap.get(id).getRentIndex(title)) {
                            System.out.print(idx + "화 ");
                        }
                        System.out.println("\n---------------------------------");
                    }
                }
            }

        } else if (n == 2) {
            System.out.print("\n추가 입고하실 만화를 입력해주세요 → ");
            String title = br.readLine();
            if(cc.comicMap.containsKey(title)){
            ComicData comic = cc.comicMap.get(title);
            ManagerController.fillBook(comic);}
            else System.out.println("\n툰스토어247에 존재하지 않는 만화입니다!\n");
        }
        else
        {
            System.out.println();
        }
    }

    public static void fillBook(ComicData comic) throws IOException {
        int n = 1;

        int[] arr = comic.getSellBookStatus();
        System.out.print("현재 재고 : ");
        for (int num : arr)
            System.out.printf("%d권[%s] ", n++, num);
        System.out.print("\n추가 입고 원하시는 화를 입력해주세요 → ");
        int num1 = Integer.parseInt(br.readLine());
        System.out.print("추가할 수량을 입력하세요 → ");
        int num2 = Integer.parseInt(br.readLine());
        arr[num1 - 1] += num2;
        System.out.println("=================================");
        System.out.printf("%s의 %d화가 %d권 만큼 입고되었습니다 !!\n\n", comic.getTitle(), num1, num2);

    }

    private static void changeComicInfo(ComicData comic) throws IOException {

        System.out.println("\n=================================");
        System.out.println("1) 제목 변경");
        System.out.println("2) 저자 변경");
        System.out.println("3) 장르 변경");
        System.out.println("4) 명대사 변경");
        System.out.println("=================================");
        System.out.print(">> 원하시는 업무를 선택하세요 → ");
        int num = Integer.parseInt(br.readLine());
        switch (num) {
            case 1:
                System.out.printf("\n현재 제목은 %s 입니다. 변경하실 내용을 입력해주세요 → ", comic.getTitle());
                comic.setTitle(br.readLine());
                break;
            case 2:
                System.out.printf("\n현재 저자는 %s 입니다. 변경하실 내용을 입력해주세요 → ", comic.getAuthor());
                comic.setAuthor(br.readLine());
                break;
            case 3:
                System.out.printf("\n현재 장르는 %s 입니다. 변경하실 내용을 입력해주세요 → ", comic.getGenre());
                comic.setGenre(br.readLine());
                break;
            case 4:
                System.out.printf("\n현재 대사는 %s 입니다. 변경하실 내용을 입력해주세요 → ", comic.getSummary());
                comic.setSummary(br.readLine());
                break;
        }
        System.out.println("\n내용 변경이 완료 되었습니다!!");

    }
    
    public static void logView() throws IOException {
        while (true) {
            System.out.println("\n========== 매출확인 ============");
            System.out.println("1) 대여로그");
            System.out.println("2) 판매로그");
            System.out.println("3) 매출확인");
            System.out.println("4) 뒤로가기");
            System.out.println("================================");
            System.out.print(">> 원하시는 업무를 선택해주세요 → ");
            int n2 = Integer.parseInt(br.readLine());
            if (n2 == 1) {
                ManagerController.rentLog();
            } else if (n2 == 2) {
                ManagerController.saleLog();
            } else if (n2 == 3) {
                ManagerController.totalLog();
            } else
            {
                System.out.println();
                break;
            }
        }
    }
    

    // 대여 로그 보기
    private static void rentLog() {
        int n = 0;
        System.out.println("\n========== 대여 로그 ============");
        for (String str : managerData.getRentInfo()) {
            System.out.print(str + " ");
            n++;
            if (n % 6 == 0) System.out.println();
        }
        System.out.println("---------------------------------");
        System.out.println("대여 매출 : " + managerData.getRent());
        System.out.println("=================================");
    }

    // 판매 로그 보기
    private static void saleLog() {
        int n = 0;
        System.out.println("\n=========== 판매 로그 ============");
        for (String str : managerData.getSaleInfo()) {
            System.out.print(str + " ");
            n++;
            if (n % 6 == 0) System.out.println();
        }
        System.out.println("---------------------------------");
        System.out.println("판매 매출 : " + managerData.getSale());
        System.out.println("=================================");
    }

    // 전체 로그 보기
    private static void totalLog() {
        System.out.println("\n=================================");
        System.out.println("대여 매출 : " + managerData.getRent());
        System.out.println("판매 매출 : " + managerData.getSale());
        System.out.println("총 매출 : " + (managerData.getRent() + managerData.getSale()));
        System.out.println("=================================");
    }

    // 파일에 ManagerData 객체 저장
    public static void saveManagerDataToFile(String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(fileName)))) {
            oos.writeObject(managerData);
            System.out.println("Manager 데이터 파일 저장 : " + fileName);
        } catch (IOException e) {
            System.err.println("Manager 데이터 저장 중 오류 : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 파일에서 ManagerData 객체 불러오기
    public static void loadManagerDataFromFile(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(fileName)))) {
            managerData = (ManagerData) ois.readObject();
            System.out.println("Manager 데이터 파일 로드 : " + fileName);
        } catch (NoSuchFileException | FileNotFoundException e) {
            System.err.println("Manager 데이터 파일이 없습니다. 최초 실행 시 없을 수 있습니다.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Manager 데이터 로딩 중 오류" + e.getMessage());
            e.printStackTrace();
        }
    }
}

// 만화의 속성 클래스
class ComicData implements Serializable {

    private String title;
    private String author;
    private String genre;
    private String summary;
    private int quantity;
    private String[] rentedBookStatus; // 각 권의 대여 상태
    private int[] sellBookStatus;

    public ComicData(String title, String author, String genre, String summary, int quantity) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.summary = summary;
        this.quantity = quantity;
        this.rentedBookStatus = new String[quantity];
        for (int i = 0; i < rentedBookStatus.length; i++)
            rentedBookStatus[i] = "○";
        this.sellBookStatus = new int[quantity];
        for (int i = 0; i < sellBookStatus.length; i++)
            sellBookStatus[i] = 5;
    }


    // Getter와 Setter 메소드
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String[] getRentedBookStatus() {
        return rentedBookStatus;
    }

    public void setRentedBookStatus(String[] rentedBookStatus) {
        this.rentedBookStatus = rentedBookStatus;
    }

    public int[] getSellBookStatus() {
        return sellBookStatus;
    }

    public void setSellBookStatus(int[] sellBookStatus) {
        this.sellBookStatus = sellBookStatus;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

// 대여, 판매, 조회 등의 기능만 담긴 추상클래스
abstract class ComicOperation {
    public abstract void totalComicView() throws IOException;

    public abstract void transactionComic(UserData uc) throws IOException;

    public abstract void recommendComic();

    public abstract void comicAdd() throws IOException;

    public abstract void saveComicMapToFile(String fileName);

    public abstract void loadComicMapFromFile(String fileName);
}

// ComicOperation 상속받아 만화 대여/판매 기능을 구현
class ComicController extends ComicOperation {

    public Map<String, ComicData> comicMap = new HashMap<>();
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    List<String> action = new ArrayList<>();
    List<String> romance = new ArrayList<>();
    List<String> comedy = new ArrayList<>();
    List<String> thriller = new ArrayList<>();
    List<String> fantasy = new ArrayList<>();

    public ComicController() {
        String fileName = "comicData.ser";

        if (!Files.exists(Paths.get(fileName))) {
            comicMap.put("나의 히어로 아카데미아", new ComicData("나의 히어로 아카데미아", "호리코시 코헤이", "액션", "저도 히어로가 될 수 있을까요..?", 7));
            comicMap.put("호리미야", new ComicData("호리미야", "하기와라 다이스케", "로맨스", "마음이 따끈따끈", 8));
            comicMap.put("귀멸의 칼날", new ComicData("귀멸의 칼날", "고토게 코요하루", "액션", "엔바시라 쿄주로 엔무!!", 8));
            comicMap.put("바람의 검심", new ComicData("바람의 검심", "와츠키 노부히로", "액션", "칼잡이 발도재 !", 4));
            comicMap.put("이누야샤", new ComicData("이누야샤", "타카하시 루미코", "액션", "안녕히 계세요 여러분! 전 이 세상의 모든 굴레와 속박을 벗어 던지고 제 행복을 찾아 떠납니다! 여러분도 행복하세요~~! !", 7));
            comicMap.put("당신은 저승님", new ComicData("당신은 저승님", "쇼탄", "로맨스", "고용해 주시겠습니까?", 7));
            comicMap.put("결혼한다는 게, 정말인가요", new ComicData("결혼한다는 게, 정말인가요", "와카키 타미키", "로맨스", "'우리 결혼하지 않을래요?'", 10));
            comicMap.put("내세에는 남남이 좋겠어", new ComicData("내세에는 남남이 좋겠어", "코니시 아스카", "로맨스", " '지금 내 눈앞에 있다는 소리야.' ", 8));
            comicMap.put("아기와 나", new ComicData("아기와 나", "라가와 마리모", "로맨스", ".운명이라..있을지도 모르지. 하지만 내가 믿는 운명은 인간이 만드는 거야.", 3));
            comicMap.put("괴짜가족", new ComicData("괴짜가족", "하마오카 켄지", "코미디", "꺄울~!, 으갸!", 8));
            comicMap.put("사카모토입니다만?", new ComicData("사카모토입니다만?", "사노 나미", "코미디", "절은 절에서나 하시죠?", 7));
            comicMap.put("사이키 쿠스오의 재난", new ComicData("사이키 쿠스오의 재난", "쇼탄", "아소 슈이치", "내 이름은 사이키 쿠스오. 초능력자다.", 9));
            comicMap.put("마루코는 아홉살", new ComicData("마루코는 아홉살", "사쿠라 모모코", "코미디", "마루코 : 할아버지가 내가 죽을때 까지 심부름 해주셨음 좋겠어.. 할아버지 : 차라리 일찍 죽는게 낫겠다.", 5));
            comicMap.put("보노보노", new ComicData("보노보노", "이가라시 미키오", "코미디", "헛소리하지마 인마", 7));
            comicMap.put("슈타인즈 게이트", new ComicData("슈타인즈 게이트", "하야시 나오타카", "스릴러", "엘 프사이 콩그루.", 8));
            comicMap.put("간츠", new ComicData("간츠", "오쿠 히로야", "스릴러", "생에 죽음의 마지막 막을 내릴때 동시에 새로운 생명 창조의 시작을 알리지", 8));
            comicMap.put("피안도", new ComicData("피안도", "마츠모토 코지", "스릴러", "괜찮아 뼈는 부러지지 않았어....", 4));
            comicMap.put("미래일기", new ComicData("미래일기", "에스노 사카에", "스릴러", "유키를 죽이려는 것들은 모조리 죽어버리면 돼!!", 6));
            comicMap.put("일하는 세포", new ComicData("일하는 세포", "하야시 나오타카", "스릴러", "어째서 환경이 나아지질 않는 거야?! 그렇게 죽도록 고생했는데, 대체 왜?", 6));
            comicMap.put("나 혼자만 레벨업", new ComicData("나 혼자만 레벨업", "추공", "판타지", "[플레이어가 되실 자격을 획득하셨습니다.]", 4));
            comicMap.put("나루토", new ComicData("나루토", "키시모토 마사시", "판타지", "호카게는 나의 꿈이니까!", 4));
            comicMap.put("블리치", new ComicData("블리치", "쿠보 타이토", "판타지", "만해.", 7));
            comicMap.put("주술회전", new ComicData("주술회전", "아쿠타미 게게", "판타지", "영역 전개.", 8));
            comicMap.put("장송의 프리렌", new ComicData("장송의 프리렌", "야마다 카네히토", "판타지", "그 백 분의 일이 너를 바꾸었다.", 3));
            comicMap.put("란마 1/2", new ComicData("란마 1/2", "타카하시 루미코", "코미디", "아.. 안경... 이거지!", 7));
            comicMap.put("원피스", new ComicData("원피스", "오다 에이치로", "액션", "나는 해적왕이 될꺼라고요!", 6));
        }
    }

    // 전체 만화 목록 출력
    @Override
    public void totalComicView() throws IOException {

        System.out.println("\n=================================");
        System.out.println("◎ 툰스토어 247 보유 만화 ◎");

        if (action.isEmpty()) {
            for (String title : comicMap.keySet()) {
                ComicData comic = comicMap.get(title);
                switch (comic.getGenre()) {
                    case "액션":
                        action.add(comic.getTitle());
                        break;
                    case "로맨스":
                        romance.add(comic.getTitle());
                        break;
                    case "코미디":
                        comedy.add(comic.getTitle());
                        break;
                    case "스릴러":
                        thriller.add(comic.getTitle());
                        break;
                    case "판타지":
                        fantasy.add(comic.getTitle());
                        break;
                }
            }
            Collections.sort(action);
            Collections.sort(romance);
            Collections.sort(comedy);
            Collections.sort(thriller);
            Collections.sort(fantasy);
        }
        print();
    }

    private void print() throws IOException {

        int n = 1;
        System.out.println("=================================");
        System.out.println("[액션]");
        for (String str : action)
            System.out.printf("%d.%s\n", n++, str);
        n = 1;
        System.out.println("=================================");
        System.out.println("[로맨스]");
        for (String str : romance)
            System.out.printf("%d.%s\n", n++, str);
        n = 1;
        System.out.println("=================================");
        System.out.println("[스릴러]");
        for (String str : thriller)
            System.out.printf("%d.%s\n", n++, str);
        n = 1;
        System.out.println("=================================");
        System.out.println("[판타지]");
        for (String str : fantasy)
            System.out.printf("%d.%s\n", n++, str);
        n = 1;
        System.out.println("=================================");
        System.out.println("[코미디]");
        for (String str : comedy)
            System.out.printf("%d.%s\n", n++, str);
        System.out.println("=================================");

        System.out.print(">> 뒤로 가기는 아무키 입력 → ");
        br.readLine();
        System.out.println();
    }

    // 선택한 만화의 상세 정보 출력
    @Override
    public void transactionComic(UserData uc) throws IOException {
        System.out.println("\n========== 대여 및 구매 ===========");
        System.out.print("작품 제목을 입력해주세요 → ");

        String title = br.readLine();

        if (comicMap.containsKey(title)) {
            System.out.println();
            int i = 1, j = 1;
            ComicData comic = comicMap.get(title);
            System.out.println("=================================");
            System.out.printf("%s님의 현재 포인트 : %d\n", uc.getName(), uc.getBalance());
            System.out.println("=================================");
            System.out.printf("제목 : %s\n", comic.getTitle());
            System.out.printf("작가 : %s\n", comic.getAuthor());
            System.out.printf("장르 : %s\n", comic.getGenre());
            System.out.printf("명대사 : %s\n", comic.getSummary());
            System.out.print("대여 재고 : ");
            for (String str : comic.getRentedBookStatus())
                System.out.printf("%d권(%s) ", i++, str);
            System.out.println();
            System.out.print("판매 재고 : ");
            for (int num : comic.getSellBookStatus())
                System.out.printf("%d권(%s) ", j++, num);
            System.out.println("\n=================================");
            System.out.print(">> 원하시는 업무를 선택해주세요 ([1]대여 [2]구매 [3]뒤로가기) → ");

            int num = Integer.parseInt(br.readLine());
            if (num == 1) {
                if (uc.getBalance() >= 1000) {
                    rentBook(title, uc);
                } else {
                    System.out.println("\n※ 고객님의 포인트가 부족합니다!\n");
                }

            } else if (num == 2) {
                if (uc.getBalance() >= 5000) {
                    buyBook(title, uc);
                } else {
                    System.out.println("\n※ 고객님의 포인트가 부족합니다!\n");
                }
            }
        } else {
            System.out.println("\n※ 해당 만화는 존재하지 않습니다!\n");
        }
    }

    // 특정 만화의 대여 로직 구현
    private void rentBook(String title, UserData uc) throws IOException {


        ComicData comic = comicMap.get(title);
        LocalDate now = LocalDate.now();
        LocalTime time = LocalTime.now();

        String[] tmp = comic.getRentedBookStatus();
        List<String> rentStatus;
        if (uc.getRentIndex(title) != null) {
            rentStatus = uc.getRentIndex(title);
            for (String str : rentStatus) {
                tmp[Integer.parseInt(str) - 1] = "X";
            }
        } else rentStatus = new ArrayList<>();

        System.out.println("\n=================================");
        System.out.printf("%s님의 현재 포인트 : %d\n", uc.getName(), uc.getBalance());
        System.out.println("=================================");
        System.out.print("대여 가능 : ");
        for (int i = 0; i < tmp.length; i++) {
            if (tmp[i].equals("○")) System.out.printf("[%d화]", i + 1);
        }
        System.out.println("\n=================================");
        System.out.print("대여 원하시는 화를 입력하세요 → ");
        int num = Integer.parseInt(br.readLine());
        if (tmp[num - 1].equals("○")) {
            tmp[num - 1] = "X";
            comic.setRentedBookStatus(tmp);

            ManagerController.managerData.setRent(1000);
            ManagerController.managerData.addRentInfo(title, "1000원", uc.getCardCompany(), uc.getCardNum(), now.toString(), time.toString());
            uc.setBalance(-1000);
            System.out.printf("\n%d화의 대여가 완료 되었습니다!\n반납 일을 지켜주세요!\n\n", num);
            rentStatus.add(Integer.toString(num));
            Collections.sort(rentStatus);
            uc.setRentIndex(title, rentStatus);
        } else {
            System.out.println("\n대여 불가능한 화입니다. 대여 가능한 화를 확인해주세요!\n");
            rentBook(title, uc);
        }
    }


    // 특정 만화의 판매 로직 구현
    private void buyBook(String title, UserData uc) throws IOException {

        ComicData comic = comicMap.get(title);
        LocalDate now = LocalDate.now();
        LocalTime time = LocalTime.now();

        System.out.println("\n=================================");
        System.out.printf("%s님의 현재 포인트 : %d\n", uc.getName(), uc.getBalance());
        int[] tmp = comic.getSellBookStatus();
        System.out.println("=================================");
        System.out.print("구매 가능 : ");
        for (int i = 0; i < tmp.length; i++) {
            if (tmp[i] > 0) System.out.printf("[%d화]", i + 1);
        }
        System.out.println("\n=================================");

        System.out.print("구매 원하시는 화를 입력하세요 → ");
        int num = Integer.parseInt(br.readLine());

        if (tmp[num - 1] < 0) {
            System.out.println("\n구매 불가능한 화입니다. 구매 가능한 화를 확인해주세요!\n");
            buyBook(title, uc);
        } else {
            tmp[num - 1]--;
            ManagerController.managerData.setSale(5000);
            ManagerController.managerData.addSaleInfo(title, "5000원", uc.getCardCompany(), uc.getCardNum(), now.toString(), time.toString());
            uc.setBalance(-5000);
            System.out.printf("\n%d화의 구매가 완료 되었습니다!\n\n", num);
        }
        comic.setSellBookStatus(tmp);

    }

    @Override
    public void comicAdd() throws IOException {

        System.out.println("\n※ 신간 정보를 입력해주세요!");
        System.out.print(">> 제목 → ");
        String title = br.readLine();
        System.out.print(">> 저자 → ");
        String author = br.readLine();
        System.out.print(">> 장르 → ");
        String genre = br.readLine();
        System.out.print(">> 명대사 → ");
        String summary = br.readLine();
        System.out.print(">> 총 화 → ");
        int quantity = Integer.parseInt(br.readLine());

        comicMap.put(title, new ComicData(title, author, genre, summary, quantity));

        System.out.printf("\n[%s] 가 정상적으로 입고 되었습니다!\n", title);

    }

    // 오늘의 추천 만화 출력 로직 구현
    @Override
    public void recommendComic() {

        Calendar today = Calendar.getInstance();

        Vector<String> vt = new Vector<>(comicMap.keySet());
        String title = vt.get(today.get(Calendar.DATE) % comicMap.size());
        ComicData comic = comicMap.get(title);
        System.out.println("=================================");
        System.out.println("♣ 오늘의 추천 만화 ♣");
        System.out.printf("[제목] %s\n", title);
        System.out.printf("[명대사] %s\n", comic.getSummary());
    }

    // ComicMap 프로그램 종료 시 객체 직렬화 저장
    @Override
    public void saveComicMapToFile(String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(fileName)))) {
            oos.writeObject(comicMap);
            System.out.println("Comic 데이터 파일 저장 : " + fileName);
        } catch (IOException e) {
            System.err.println("Comic 데이터 저장 중 오류 : " + e.getMessage());
            System.out.println(e);
        }
    }

    // ComicMap 데이터 시작 시 객채 역직렬화 로딩
    @Override
    public void loadComicMapFromFile(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(fileName)))) {
            comicMap = (Map<String, ComicData>) ois.readObject();
            System.out.println("Comic 데이터 파일 로드 : " + fileName);
        } catch (NoSuchFileException | FileNotFoundException e) {
            System.err.println("Comic 데이터 파일이 없습니다. 최초 실행 시 없을 수 있습니다.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Comic 데이터 로딩 중 오류" + e.getMessage());
            e.printStackTrace();
        }
    }
}

class ToonStore247 {

    static String accessedID;
    // ManagerController mc = new ManagerController();
    // ManagerController 는 하나로 공유해야 하니 Static으로 선언
    ComicController cc = new ComicController();
    UserController uc = new UserController();

    // UserData 초기 로딩 코드
    public void initData() {
        uc.loadUserMapFromFile("userData.ser");
        cc.loadComicMapFromFile("comicData.ser");
    }

    public void homeMenu() {

        try {
            int n = ManagerController.HomeUI();

            switch (n) {
                case 1:
                    accessedID = uc.login();
                    if (accessedID.equals("admin")) {
                        managerMenu();
                    }
                    else if (uc.userMap.containsKey(accessedID))
                        customerMenu();
                    else {
                        System.out.println("존재하지 않는 아이디입니다!\n");
                        accessedID = "";
                    }
                    break;
                case 2:
                        uc.signUp();
                    break;
                case 3:
                    uc.saveUserMapToFile("userData.ser");
                    cc.saveComicMapToFile("comicData.ser");
                    ManagerController.saveManagerDataToFile("managerData.ser");
                    System.exit(-1);
            }
        } catch (Exception e) {
            System.out.println(e);
            homeMenu();
        }
    }

    public void customerMenu() {
        
        UserData ud = uc.userMap.get(accessedID);

        System.out.printf("오늘은 %s | [%s님 환영합니다!]\n", LocalDate.now(), ud.getName());
        cc.recommendComic();

        try {
            int n = ManagerController.CustomerUI();

            switch (n) {
                case 1:
                    cc.totalComicView();
                    customerMenu();
                    break;
                case 2:
                    cc.transactionComic(ud);
                    customerMenu();
                    break;
                case 3:
                    uc.chargePoint(accessedID);
                    customerMenu();
                    break;
                case 4:
                    uc.userView(accessedID);
                    customerMenu();
                    break;
                case 5:
                    System.out.println();
                    homeMenu();
                    accessedID = "";
                    break;
                case 6:
                    uc.saveUserMapToFile("userData.ser");
                    cc.saveComicMapToFile("comicData.ser");
                    ManagerController.saveManagerDataToFile("managerData.ser");
                    System.out.print("프로그램을 종료합니다.");
                    System.exit(-1);  // 프로그램 종료
                    break;
                default:
                    System.out.println("잘못된 선택입니다. 다시 입력해주세요.");
            }
        } catch (Exception e) {
            System.out.println(e);
            customerMenu();
        }
    }

    public void managerMenu() {

        try {
            int num = ManagerController.ManagerUI();

            switch (num) {
                case 1:
                    ManagerController.manageComicInfo();
                    managerMenu();
                    break;

                case 2:
                    ManagerController.manageComicInventory();
                    managerMenu();
                    break;

                case 3:
                    ManagerController.logView();
                    managerMenu();
                    break;
                case 4:
                    System.out.println();
                    homeMenu();
                    accessedID = "";
                    break;
                case 5:
                    uc.saveUserMapToFile("userData.ser");
                    cc.saveComicMapToFile("comicData.ser");
                    ManagerController.saveManagerDataToFile("managerData.ser");
                    System.exit(-1);
            }
        } catch (Exception e) {
            System.out.println(e);
            managerMenu();
        }
    }

}

public class Main {
    public static void main(String[] args) throws IOException {

        ToonStore247 ob = new ToonStore247();
        // UserData 프로그램 시작 시 로딩
        ob.initData();
        while (true) ob.homeMenu();
    }
}

