# Java 기초 실습 정리 (week2)

---

## 1. src vs out

* src: 개발자가 작성한 소스 코드
* out: 컴파일 후 생성된 실행 파일(.class)

프로그램 실행 시 src 코드를 컴파일하여 out에서 실행된다.

한 줄 요약
→ src는 코드, out은 실행 결과물

---

## 2. 변수명 규칙

* 숫자로 시작 불가 → 컴파일 에러 발생
* 자바 예약어 사용 불가

변수명 규칙은 단순한 스타일이 아니라 문법 규칙이다.

느낀점
단순 규칙이라고 생각했지만, 실제로는 컴파일 자체에 영향을 준다는 점이 중요하다.

---

## 3. 참조형 (Reference Type)

* 변수에는 값이 아니라 주소값이 저장됨
* 실제 데이터는 힙 메모리에 저장됨

생성 방식

```java
String a = new String("Hello!");
String b = "Hello!";
String c = "Hello!";
```

* new 사용 → 새로운 객체 생성
* 리터럴 사용 → 동일한 문자열 공유

비교 결과

```java
a == b → false  
b == c → true
```

== 연산자는 주소를 비교한다.

한 줄 요약
→ 참조형은 주소를 비교하며, 생성 방식에 따라 결과가 달라진다.

---

## 4. 형변환 (TypeCasting)

* 작은 타입 → 큰 타입: 자동 형변환
* 큰 타입 → 작은 타입: 강제 형변환

```java
short g = (short)(e + f);
```

연산 결과는 기본적으로 int 타입으로 계산된다.

한 줄 요약
→ 타입 변환 시 상황에 따라 강제 형변환이 필요하다.

---

## 5. 오버플로우 (Overflow)

* 데이터 범위를 초과하면 값이 왜곡된다

```java
int a = 128;
byte b = (byte)a; // -128
```

작은 타입으로 변환 시 데이터 손실이 발생할 수 있다.

한 줄 요약
→ 범위를 넘는 형변환 시 값이 변경된다.

---

## 6. 변수 (Variables)

* 데이터를 저장하는 공간
* 선언 후 값 할당 및 재할당 가능

한 줄 요약
→ 변수는 데이터를 저장하고 변경할 수 있는 공간이다.

---

## 7. 논리 연산자 및 단락 평가

논리 연산자

* && : 두 조건이 모두 true일 때 true
* || : 하나라도 true이면 true
* ! : 값을 반대로 변경

단락 평가

* false && 조건 → 뒤 조건 실행되지 않음
* true || 조건 → 뒤 조건 실행되지 않음

앞의 조건으로 결과가 결정되면 뒤의 조건은 실행되지 않는다.

한 줄 요약
→ 논리 연산은 조건을 결합하며, 단락 평가로 불필요한 연산을 생략한다.

---

## 8. Map (HashMap)

* 키와 값의 쌍으로 데이터를 저장
* 키는 중복 불가, 값은 중복 가능
* 순서 없음

```java
HashMap<String, String> map = new HashMap<>();
map.put("age", "30");
map.put("mbti", "INFP");

System.out.println(map.get("age"));
```

한 줄 요약
→ Map은 키를 통해 값을 조회하는 자료구조이다.

---

## 9. 메서드와 리스트 활용

```java
import java.util.ArrayList;

public class MethodExample2 {

    public static void main(String[] args) {

        ArrayList list_1 = new ArrayList<>();
        list_1.add(10);
        list_1.add(100);

        printListElements(list_1);

    }

    static void printListElements(ArrayList list) {

        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }
}
```

설명

* ArrayList에 데이터를 저장하고 메서드에 전달
* 메서드 내부에서 반복문을 사용하여 요소 출력
* 메서드를 통해 코드 재사용 가능

한 줄 요약
→ 메서드를 활용하면 데이터를 전달받아 반복적으로 처리할 수 있다.

