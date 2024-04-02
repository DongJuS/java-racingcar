package racingcar;

import camp.nextstep.edu.missionutils.Randoms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static camp.nextstep.edu.missionutils.Console.readLine;

class InputView {
    public String inputCar() {
        System.out.println("input the name of cars. (names will be spilt by comma");
        String input;
        input = readLine();
        if (input.length() < 5) {
            throw new IllegalArgumentException("the name of car should be less than 5");
        }
        return input;
    }

    public int inputTry() {
        System.out.println("the number of times?");
        int Try = Integer.parseInt((readLine()));
        return Try;
    }
}
class ResultView {
        private static final String FORWARD_SIGN = "-";

        public void printResult(){
            System.out.println("실행 결과\n");
        }
        public void showResult(List<Car> carList){
            for(int i =0; i< carList.size(); i++){
                System.out.print(carList.get(i).getName()+" : ");
                System.out.println(FORWARD_SIGN.repeat(carList.get(i).getPosition()));
            }
            System.out.println();
        }
}
class Car{
    private String name;
    private int position;
    static final String COMMA = ",";
    private static final int FORWARD_STANDARD = 4;
    private Car car;

    Car(String name, int position){
        this.name = name;
        this.position = 0;
    }
    public Car(String name) {
        this.name = name;
        this.position = 0;
    }
    public Car(int position){
        this.position =0;
    }
    public Car(){};

    public List<String> splitName(String names) {
        return new ArrayList<>(Arrays.asList(names.split(COMMA)));
    }

    boolean canForward(int random){
        return random >= FORWARD_STANDARD;
    }
    void move(){
        this.position++;
    }
    void makeForward(Car car, int random){
        
        if(canForward(random)){
            car.move();
        }
    }
    int getPosition(){
        return this.position;
    }

    boolean isSamePosition(int max){
        return this.position ==max;
    }
    public int getMax(int max){
        return Math.max(max, this.position);
    }
    public String getName(){
        return this.name;
    }


}

class RacingGame{
    public void race(List<Car> carList) {
        int random = 0;
        for (Car car : carList) {
            random = Randoms.pickNumberInRange(0,9);
            car.makeForward(car,random);
        }
    }
}
class CarFactory {
    public CarFactory(){};
    List<Car> createCars(String names){
        List<Car> carList = new ArrayList<>();
        Car car = new Car();
        for(String name : car.splitName(names)){
            carList.add(new Car(name));
        }
        return carList;
    }
}
class Winner {
    public List<String> findWinner(List<Car> carList) {
        return exploreWinnerName(carList, findMax(carList));
    }
    public int findMax(List<Car> carList){
        int max = 0;
        for (Car car : carList) {
            max = new Car(car.getPosition()).getMax(max);
        }
        return max;
    }
    private List<String> exploreWinnerName(List<Car> carList, int max) {
        List<String> winnerNames = new ArrayList<>();

        for (Car car : carList) {
            putWinnerNames(winnerNames, max, car);
        }
        return winnerNames;
    }
    private List<String> putWinnerNames(List<String> winnerNames, int max, Car car){
        if (new Car(car.getPosition()).isSamePosition(max)) {
            winnerNames.add(car.getName());
        }
        return winnerNames;
    }
}

public class Application {
    public static void main(String[] args) {

        InputView inputView = new InputView();
        ResultView resultView = new ResultView();
        RacingGame racing = new RacingGame();
        Car car = new Car();
        CarFactory carFcty = new CarFactory();
        Winner winner = new Winner();

        List<Car> carList = carFcty.createCars(inputView.inputCar());
        int tryCount = inputView.inputTry();

        resultView.printResult();

        for (int i = 0; i < tryCount; i++) {
            racing.race(carList);
            resultView.showResult(carList);
        }
        for(String win : winner.findWinner(carList)){
            System.out.print("winner is : "+winner.findWinner(carList));
        }

    }
}


/* 만약에 인터넷에 있던 코드를 불러올거라면 이거와 end
package racingcar;

import camp.nextstep.edu.missionutils.Randoms;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static camp.nextstep.edu.missionutils.Console.readLine;

class GameController {
    private static final int MIN_CAR_NAME_LENGTH = 1;
    private static final int MAX_CAR_NAME_LENGTH = 5;
    private static final int MIN_ROUND_COUNT = 1;

    private final List<Car> cars = new ArrayList<Car>();
    private int round;

    public void run(Scanner scanner) {
        setCars(scanner);
        setRound(scanner);
        printResult();

    }


    private void setCars(Scanner scanner) {
        List<String> names;
        String[] input;

        System.out.println("경주할 자동차 이름을 입력하세요.(이름은 쉼표(,) 기준으로 구분");
        input = scanner.nextLine().split(",");
        names = Arrays.stream(input).map(String::trim).collect(Collectors.toList());
        validateNames(names);
        names.forEach(name -> cars.add(new Car(name)));
    }

    private void setRound(Scanner scanner) {
        String input;

        System.out.println("시도할 회수는 몇회인가요?");
        input = scanner.nextLine().trim();
        validateRound(input);
        round = Integer.parseInt(input);
        System.out.println();
    }

    private void printResult() {
        HashMap<String, Integer> raceResult = new HashMap<>();

        printRacingResult(raceResult);
        printWinners(raceResult);
    }

    private void printRacingResult(HashMap<String, Integer> raceResult) {
        System.out.println("실행 결과");
        for (int i = 0; i < round; i++) {
            race(raceResult);
            System.out.println();
        }
    }

    private void race(HashMap<String, Integer> raceResult) {
        cars.forEach(car -> {
            car.run();
            String name = car.getName();
            int position = car.getPosition();
            raceResult.put(name, position);
            printEachCarRacingResult(name, position);
        });
    }

    private void printEachCarRacingResult(String name, int position) {
        StringBuilder msg = new StringBuilder(name + " : ");
        Optional<String> formattedPosition = Stream.generate(() -> "-").limit(position).reduce((a, b) -> a + b);
        formattedPosition.ifPresent(msg::append);
        System.out.println(msg);
    }

    private void printWinners(HashMap<String, Integer> raceResult) {
        int maxPosition = Collections.max(raceResult.values());

        System.out.println("최종 우승자");
        System.out.println(raceResult.entrySet().stream().filter(m -> m.getValue() == maxPosition).map(Map.Entry::getKey).collect(Collectors.joining(", ")));
    }

    private void validateNames(List<String> names) {
        HashSet<String> nameSet = new HashSet<>();
        names.forEach(name -> {
            validateName(name);
            checkOverlappingName(nameSet, name);
        });
    }

    private void validateName(String name) {
        if (name.length() < MIN_CAR_NAME_LENGTH) {
            throw new IllegalArgumentException("[ERROR] 자동차의 이름은 1자 이상이어야 한다.");
        }
        if (name.length() > MAX_CAR_NAME_LENGTH) {
            throw new IllegalArgumentException("[ERROR] 자동차의 이름은 5자 이하여야 한다.");
        }
    }

    private void checkOverlappingName(HashSet<String> nameSet, String name) {
        if (nameSet.contains(name)) {
            throw new IllegalArgumentException("[ERROR] 자동차의 이름은 고유해야만 한다.");
        }
        nameSet.add(name);
    }

    private void validateRound(String round) {
        try {
            Integer.parseInt(round);
        } catch (Exception e) {
            throw new IllegalArgumentException("[ERROR] 시도 횟수는 숫자여야 한다.");
        }
        if (Integer.parseInt(round) < MIN_ROUND_COUNT) {
            throw new IllegalArgumentException("[ERROR] 시도 횟수는 1 이상이어야 한다.");
        }
    }

    class RandomUtils {
    private static final Random RANDOM = new Random();

    private RandomUtils() {
    }
    public static int nextInt(final int startInclusive, final int endInclusive) {
        if (startInclusive > endInclusive) {
            throw new IllegalArgumentException();
        }

        if (startInclusive < 0) {
            throw new IllegalArgumentException();
        }

        if (startInclusive == endInclusive) {
            return startInclusive;
        }

        return startInclusive + RANDOM.nextInt(endInclusive - startInclusive + 1);
    }
    }
    public class Car {
        private final int MIN_RANDOM_VALUE = 0;
        private final int MAX_RANDOM_VALUE = 9;
        private final int FORWARD_CRITERIA = 4;
        private final String name;
        private int position = 0;

            public Car(String name) {
                this.name = name;
            }
        public String getName() {
            return name;
        }

        public int getPosition() {
            return position;
        }
        public void run() {
            int randomValue = GameController.RandomUtils.nextInt(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
            if (randomValue >= FORWARD_CRITERIA) {
                position += 1;
            }
        }

    }


}



public class Application {


    public static void main(String[] args) {
        // TODO: 프로그램 구현
        Scanner scanner = new Scanner(System.in);
        GameController gameController = new GameController();
        try {
            gameController.run(scanner);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }





}
*/
/*
여기까지 내가 처음에 한 부분

package racingcar;

import camp.nextstep.edu.missionutils.Randoms;

import java.util.HashMap;
import java.util.StringTokenizer;

import static camp.nextstep.edu.missionutils.Console.readLine;


class CarsInfo {
    public String carNm;
    public String mv;
    CarsInfo(){};
    CarsInfo(String carNm, String mv){
        this.carNm = carNm;
        this.mv= mv;
    }
}



public class Application {
    public static String[] cars= new String[10];
    public static void move(){

        HashMap<String, Integer> map = new HashMap<String,Integer>(){};


        System.out.println("how many times would you try?");
        int times = Integer.parseInt(readLine());
//        System.out.println(times==5);  times가 integer인지 확인
        Randoms.pickNumberInRange(0,9);

        String carYet = readLine(); //아직 쉼표로 구분 전
        StringTokenizer st = new StringTokenizer(carYet,",");
        int num=0;

        System.out.println("There are " + cars.length + " gremlins.");
        while(st.hasMoreTokens()){
//            System.out.println(st.nextToken());
            cars[num] = st.nextToken();
            num++; //이 횟수로 배열에 저장
            //cars에 저장되긴 했다.
            map.put(cars[num],0);
        }
        //출력문
        for(int i=0;i<num;i++){
            System.out.println(cars[i]);
        }


    }
    public static void main(String[] args) {
        // TODO: 프로그램 구현

        System.out.println("test");
        move();
        CarsInfo car = new CarsInfo(cars[0],"5");


    }
}


// 동적 전진 또는 멈춤 횟수 - 인자로 주기
// 동적 자동차 n대 부여
// 자동차 이름 부여
//자동차 이름은 쉼표를 기준으로 구분 + 이름은 5자 이하
//전진하는 조건은 0~9사이 중 4이상 무작위 값
//마지막에 어떤 자동차가 우승인지 print
//우승자가 여러명이면 쉼표로 구분
// 사용자가 잘못된 값을 입력하면 illegalArgumentException 발생 후 종료
*//*
*/
/*

end를 풀면 된다.
*/
