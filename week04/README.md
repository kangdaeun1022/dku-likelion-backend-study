# Week05 - Java 객체지향 / 예외처리

## 학습 내용

이번 스터디에서는 Java 객체지향 프로그래밍과 예외 처리에 대해 학습하였다.

클래스와 객체의 개념부터 상속, 캡슐화, 오버라이딩/오버로딩 등을 직접 코드로 구현해보았고,  
예외 처리를 통해 프로그램 실행 중 발생할 수 있는 오류 상황을 처리하는 방법을 실습하였다.

## 1. 객체지향 프로그래밍

### 클래스와 객체

클래스는 객체를 생성하기 위한 설계도 역할을 하며,  
객체는 클래스를 기반으로 생성된 실제 데이터이다.

```java
BankAccount bankAccount = new BankAccount();
```

`BankAccount` 클래스를 기반으로 객체를 생성하였다.

### 멤버 변수와 메서드

멤버 변수는 객체의 데이터를 저장하고,  
메서드는 객체의 기능을 정의한다.

```java
private int bankCode;
private int accountNo;
private String owner;
private int balance;

public void deposit() {}
public void withdraw() {}
```

### 캡슐화

멤버 변수를 `private`으로 선언하여 외부 접근을 제한하고,  
Getter / Setter 메서드를 통해 접근하도록 구현하였다.

```java
private int password;

public void changePassword(int password) {
    this.password = password;
}
```

이를 통해 객체 내부 데이터를 안전하게 관리할 수 있다.

## 2. 생성자

생성자는 객체 생성 시 초기값을 설정하는 메서드이다.

```java
BankAccount() {

}
```

```java
BankAccount(
        int bankCode,
        int accountNo,
        String owner,
        int balance,
        int password,
        boolean isDormant
)
```

객체 생성과 동시에 필요한 값을 초기화할 수 있도록 구현하였다.

## 3. 상속

상속은 부모 클래스의 기능을 자식 클래스가 물려받는 개념이다.

```java
public class SavingsAccount extends BankAccount
```

`SavingsAccount`, `DollarAccount`, `SubscriptionAccount` 클래스는  
`BankAccount` 클래스를 상속받아 구현하였다.

이를 통해 코드 재사용성과 확장성을 높일 수 있었다.

## 4. 오버라이딩 / 오버로딩

### 오버라이딩

부모 클래스의 메서드를 자식 클래스에서 재정의하는 방식이다.

```java
public void deposit() {

}
```

`DollarAccount` 클래스에서 부모 클래스의 메서드를 재정의하였다.

### 오버로딩

같은 이름의 메서드를 매개변수만 다르게 여러 개 정의하는 방식이다.

```java
void inquiry(double currencyRate) {}
```

기존 메서드와 다른 파라미터를 사용하여 새로운 메서드를 구현하였다.

## 5. 예외 처리

### try-catch-finally

```java
try {

} catch (Exception e) {

} finally {

}
```

- `try` : 예외 발생 가능 코드
- `catch` : 예외 처리
- `finally` : 항상 실행되는 영역

### ArithmeticException 실습

```java
int a = 10;
int b = 0;
int c = a / b;
```

0으로 나누는 연산을 통해 예외 발생 상황을 확인하였다.

### 다양한 예외 처리

```java
catch (IndexOutOfBoundsException ioe)
catch (IllegalArgumentException iae)
catch (Exception e)
```

여러 종류의 예외를 상황에 맞게 처리하도록 구현하였다.

## 궁금했던 점과 해결 과정

### 궁금했던 점 1

왜 멤버 변수를 `private`으로 선언하고 Getter / Setter를 사용하는지 궁금했다.

### 해결

외부에서 객체 내부 데이터를 직접 수정하면 데이터 무결성이 깨질 수 있기 때문에,  
Getter / Setter를 통해 필요한 기능만 제한적으로 제공한다는 점을 이해할 수 있었다.

### 궁금했던 점 2

오버라이딩과 오버로딩의 차이가 헷갈렸다.

### 해결

오버라이딩은 부모 클래스의 메서드를 재정의하는 것이고,  
오버로딩은 같은 이름의 메서드를 매개변수만 다르게 여러 개 만드는 것이라는 차이를  
코드로 직접 구현하며 이해할 수 있었다.

### 궁금했던 점 3

`finally`가 왜 필요한지 궁금했다.

### 해결

예외 발생 여부와 관계없이 반드시 실행되어야 하는 코드가 존재하기 때문에,  
리소스 정리나 마무리 작업을 위해 `finally`를 사용한다는 점을 이해할 수 있었다.

## 느낀 점

객체지향 프로그래밍은 단순히 기능 구현이 아니라,  
객체 간 역할과 관계를 설계하는 과정이라는 점을 이해할 수 있었다.

또한 예외 처리를 통해 프로그램 실행 중 발생할 수 있는 오류 상황에 안정적으로 대응하는 방법을 학습할 수 있었다.
