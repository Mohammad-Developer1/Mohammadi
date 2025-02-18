import { useState } from "react";

const Login = () => {
    const [formData, setFormData] = useState({
        username: "",
        password: "",
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            const response = await fetch("http://localhost:8080/api/auth/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json", // حتما application/json باشد
                },
                body: JSON.stringify({
                    username: formData.username,
                    password: formData.password,
                }),
            });

            if (!response.ok) {
                throw new Error("Invalid login credentials");
            }

            const data = await response.text();
            alert("Login successful: " + data);
        } catch (error) {
            alert("Login failed. Check your credentials.");
        }
    };



    return (
        <div style={{
            minHeight: "100vh",
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
            backgroundColor: "#1e1e1e", // پس زمینه خاکستری تیره
        }}>
            <div style={{
                backgroundColor: "#2a2a2a", // جعبه لاگین با رنگ خاکستری متوسط
                padding: "2rem",
                borderRadius: "15px",
                boxShadow: "0px 4px 15px rgba(255, 215, 0, 0.3)", // سایه طلایی
                width: "100%",
                maxWidth: "400px",
                textAlign: "center",
            }}>
                <h2 style={{
                    color: "#d4af37", // رنگ طلایی
                    fontSize: "24px",
                    fontWeight: "bold",
                    marginBottom: "20px"
                }}>ورود به حساب</h2>
                <form onSubmit={handleSubmit} style={{ display: "flex", flexDirection: "column", gap: "15px" }}>
                    <div>
                        <label style={{ display: "block", color: "#f1f1f1", marginBottom: "5px" }}>نام کاربری</label>
                        <input
                            type="text"
                            name="username"
                            placeholder="نام کاربری"
                            value={formData.username}
                            onChange={handleChange}
                            required
                            style={{
                                width: "100%",
                                padding: "12px",
                                border: "1px solid #d4af37",
                                borderRadius: "5px",
                                backgroundColor: "#333",
                                color: "#fff",
                                outline: "none",
                                transition: "border 0.3s",
                            }}
                            onFocus={(e) => (e.target as HTMLInputElement).style.border = "2px solid #ffd700"}
                            onBlur={(e) => (e.target as HTMLInputElement).style.border = "1px solid #d4af37"}
                        />
                    </div>
                    <div>
                        <label style={{ display: "block", color: "#f1f1f1", marginBottom: "5px" }}>رمز عبور</label>
                        <input
                            type="password"
                            name="password"
                            placeholder="رمز عبور"
                            value={formData.password}
                            onChange={handleChange}
                            required
                            style={{
                                width: "100%",
                                padding: "12px",
                                border: "1px solid #d4af37",
                                borderRadius: "5px",
                                backgroundColor: "#333",
                                color: "#fff",
                                outline: "none",
                                transition: "border 0.3s",
                            }}
                            onFocus={(e) => (e.target as HTMLInputElement).style.border = "2px solid #ffd700"}
                            onBlur={(e) => (e.target as HTMLInputElement).style.border = "1px solid #d4af37"}
                        />
                    </div>
                    <button type="submit" style={{
                        width: "100%",
                        backgroundColor: "#d4af37",
                        color: "#1e1e1e",
                        padding: "12px",
                        borderRadius: "5px",
                        border: "none",
                        fontWeight: "bold",
                        cursor: "pointer",
                        transition: "background-color 0.3s",
                    }}
                            onMouseOver={(e) => (e.target as HTMLButtonElement).style.backgroundColor = "#ffd700"}
                            onMouseOut={(e) => (e.target as HTMLButtonElement).style.backgroundColor = "#d4af37"}
                    >
                        ورود
                    </button>
                </form>
                <p style={{
                    color: "#aaa",
                    fontSize: "14px",
                    marginTop: "15px"
                }}>
                    حساب کاربری ندارید؟ <a href="/signup" style={{
                    color: "#d4af37",
                    textDecoration: "none"
                }}
                                           onMouseOver={(e) => (e.target as HTMLAnchorElement).style.textDecoration = "underline"}
                                           onMouseOut={(e) => (e.target as HTMLAnchorElement).style.textDecoration = "none"}
                >ثبت‌نام کنید</a>
                </p>
            </div>
        </div>
    );
};

export default Login;
