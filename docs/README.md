# Exactly User Guide

Welcome to **Exactly**, your personal CLI and GUI chatbot for managing tasks efficiently.

## Features

- Add a **Todo** task
- Add a **Deadline** task
- Add an **Event** task
- **List** all tasks
- **Mark** a task as done
- **Unmark** a task as not done
- **Delete** a task
- **Find** tasks by keyword
- **Exit** the application

## Getting Started

1. Clone or download the repository to your local machine.
2. Build the project with your preferred build tool (e.g., Gradle or Maven).
3. Run the GUI:
   ```bash
   java -jar Exactly.jar
   ```
   Or run the CLI:
   ```bash
   java -cp build/libs/Exactly.jar exactly.Exactly data/exactly.txt
   ```

## Usage

### 1. Add a Todo

Type:

```bash
todo <description>
```

Example:

```bash
todo Buy groceries
```

Expected response:

```
 Got it. I've added this task:
   [T][ ] Buy groceries
 Now you have 1 tasks in the list!
```

### 2. Add a Deadline

Type:

```bash
deadline <description> /by <yyyy-MM-dd>
```

Example:

```bash
deadline Submit report /by 2025-04-30
```

Expected response:

```
 Got it. I've added this task:
   [D][ ] Submit report (by: Apr 30 2025)
 Now you have 2 tasks in the list!
```

### 3. Add an Event

Type:

```bash
event <description> /from <start> /to <end>
```

Example:

```bash
event Team meeting /from 10:00 /to 11:00
```

Expected response:

```
 Got it. I've added this task:
   [E][ ] Team meeting (from: 10:00 to: 11:00)
 Now you have 3 tasks in the list!
```

### 4. List tasks

Type:

```bash
list
```

Expected response:

```
 Here are the tasks in your list:
 1. [T][ ] Buy groceries
 2. [D][ ] Submit report (by: Apr 30 2025)
 3. [E][ ] Team meeting (from: 10:00 to: 11:00)
```

### 5. Mark a task as done

Type:

```bash
mark <task number>
```

Example:

```bash
mark 1
```

Expected response:

```
 Awesome! I've marked this task as done:
   [T][X] Buy groceries
```

### 6. Unmark a task

Type:

```bash
unmark <task number>
```

Expected response:

```
 Got it! I've marked this task as not done yet:
   [T][ ] Buy groceries
```

### 7. Delete a task

Type:

```bash
delete <task number>
```

Expected response:

```
 Noted. I've removed this task:
   [T][ ] Buy groceries
 Now you have 2 tasks in the list.
```

### 8. Find tasks by keyword

Type:

```bash
find <keyword>
```

Expected response (example):

```
 Here are the matching tasks in your list:
 1. [D][ ] Submit report (by: Apr 30 2025)
```

### 9. Exit

Type:

```bash
bye
```

Expected response:

```
 Bye! Keep crushing it and never settle for less!
```
