import java.util.ArrayList;
import java.util.Scanner;

public class TodoController {
    private Scanner scanner;
    private ArrayList<Todo> todos;
    private long todosLastId;

    public TodoController(Scanner scanner) {
        this.scanner = scanner;
        this.todos = new ArrayList<>();
        this.todosLastId = 0;
    }

    public void add() {
        long id = ++todosLastId;

        System.out.print("할일 : ");
        String content = scanner.nextLine().trim();

        Todo todo = new Todo(id, content);
        todos.add(todo);

        System.out.printf("%d번 할일이 생성되었습니다.%n", id);
    }

    public void list() {
        System.out.println("번호 / 내용");

        for (Todo todo : todos) {
            System.out.printf("%d / %s%n",
                    todo.getId(),
                    todo.getContent());
        }
    }

    public void del() {
        System.out.print("삭제할 할일의 번호 : ");
        long id = Long.parseLong(scanner.nextLine().trim());

        boolean removed = todos.removeIf(todo -> todo.getId() == id);

        if (!removed) {
            System.out.printf("%d번 할일은 존재하지 않습니다.%n", id);
            return;
        }

        System.out.printf("%d번 할일이 삭제되었습니다.%n", id);
    }

    public void modify() {
        System.out.print("수정할 할일의 번호 : ");
        long id = Long.parseLong(scanner.nextLine().trim());

        Todo foundTodo = todos.stream()
                .filter(todo -> todo.getId() == id)
                .findFirst()
                .orElse(null);

        if (foundTodo == null) {
            System.out.printf("%d번 할일은 존재하지 않습니다.%n", id);
            return;
        }

        System.out.printf("기존 할일 : %s%n", foundTodo.getContent());
        System.out.print("새 할일 : ");

        String newContent = scanner.nextLine().trim();
        foundTodo.setContent(newContent);

        System.out.printf("%d번 할일이 수정되었습니다.%n", id);
    }
}