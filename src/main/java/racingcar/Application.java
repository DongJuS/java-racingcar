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

