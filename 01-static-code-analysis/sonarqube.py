import sqlite3

def unsafe_login(user_input):
    # Vulnerable to SQL Injection because input is concatenated directly into the query.
    conn = sqlite3.connect('users.db')
    cursor = conn.cursor()
    query = "SELECT * FROM users WHERE username = '" + user_input + "'"
    cursor.execute(query)
    result = cursor.fetchone()
    conn.close()
    return result

def unsafe_eval(expression):
    # Vulnerable to arbitrary code execution via eval.
    return eval(expression)

def main():
    # Get unsanitized user input.
    username = input("Enter username: ")
    result = unsafe_login(username)
    if result:
        print("Login successful!")
    else:
        print("Login failed!")

    # Demonstrate unsafe eval usage.
    expr = input("Enter a Python expression to evaluate: ")
    print("Result:", unsafe_eval(expr))

if __name__ == "__main__":
    main()
