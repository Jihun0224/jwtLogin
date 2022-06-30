import React, { useState } from 'react'
import Paper from '@mui/material/Paper';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import Avatar from "@mui/material/Avatar";
import kakaoImg from "../assets/images/kakao.png";
import NaverImg from "../assets/images/naver.png";
import IconButton from "@mui/material/IconButton";
import { Link } from "react-router-dom";
import axios from 'axios';

function Login() {
    const { Kakao } = window;
    const theme = createTheme({
        palette: {
            primary: {
                main: '#02343F',
                contrastText: "#F0EDCC"
            },
            secondary: {
                main: '#F0EDCC',
            }
        }
    })
    const [loginInfo, setLoginInfo] = useState({ name: '', password: '' });
    const onInput = e => {
        const { name, value } = e.target;
        // const name = e.target.name , const value = e.target.value인 것임.

        setLoginInfo(prev => {
            return { ...prev, [name]: value };
        });
    }
    const kakaoLoginClickHanndler = ({ history }) => {
        Kakao.Auth.login({
            success: function (authObj) {
                console.log(authObj.access_token);
                axios.post("http://localhost:8080/api/kakao", {
                    access_token: authObj.access_token
                }, {
                    headers: {
                        "Content-Type": "application/json",
                    },
                })
                    .then(res => res.json())
                    .then(res => {
                        console.log(res);
                        localStorage.setItem("user", res)
                        history.push("/")
                    })
            },
            fail: function (err) {
                console.log(JSON.stringify(err));
                alert("다시 입력해주세요.")
            }
        })
    }
    const naverLoginClickHanndler = e => {
        Kakao.Auth.login({
            success: function (authObj) {
                console.log(authObj.access_token);
                axios.post("http://localhost:8080/api/kakao", {
                    access_token: authObj.access_token
                }, {
                    headers: {
                        "Content-Type": "application/json",
                    },
                })
                    .then(res => res.json())
                    .then(res => {
                        console.log(res);
                    })
            },
            fail: function (err) {
                console.log(JSON.stringify(err));
                alert("다시 입력해주세요.")
            }
        })
    }
    const onSubmit = (e) => {
        e.preventDefault();
        var url = "http://localhost:8080/api/authenticate"
        axios.post(url, {
            username: loginInfo.name,
            password: loginInfo.password
        })
            .then((response) => {
                console.log(response);
            }).catch((error) => {
                console.log(error);
            })
    }
    return (
        <div className="login-form">
            <Paper elevation={3} id="login-btns-wrap">
                <div id="loginForm">
                    <h2>로그인</h2>
                    <form onSubmit={onSubmit}>
                        <ThemeProvider theme={theme}>
                            <TextField
                                style={{
                                    backgroundColor: "white",
                                    width: "320px"
                                }}
                                margin="dense"
                                label="Id"
                                name="name"
                                variant="outlined"
                                onInput={onInput} />
                        </ThemeProvider>
                        <ThemeProvider theme={theme}>
                            <TextField
                                style={{
                                    backgroundColor: "white",
                                    width: "320px"
                                }}
                                margin="dense"
                                label="Password"
                                type="password"
                                name="password"
                                variant="outlined"
                                onInput={onInput} />
                        </ThemeProvider>
                        <br />
                        <ThemeProvider theme={theme}>
                            <Button
                                id="login-btn"
                                type="submit"
                                color="primary"
                                variant="contained"
                                size="large">
                                로그인
                            </Button>
                        </ThemeProvider>
                    </form>
                    <div id="find-user-info">
                        아이디 찾기 | 비밀번호 찾기
                    </div>
                    <div id="social-login">
                        <ul>
                            <h4>SNS 계정으로 로그인 하기</h4>
                            <li>
                                <IconButton onClick={kakaoLoginClickHanndler}>
                                    <Avatar
                                        alt="카카오로 로그인하기"
                                        src={kakaoImg}
                                        sx={{ width: 45, height: 45 }}
                                    />
                                </IconButton>

                            </li>
                            <li>
                                <IconButton onClick={kakaoLoginClickHanndler}>
                                    <Avatar
                                        alt="네이버로 로그인하기"
                                        src={NaverImg}
                                        sx={{ width: 45, height: 45 }}
                                    />
                                </IconButton>
                            </li>
                        </ul>
                    </div>
                    <div id="sign-up-wrap">
                        <h5>아직 회원이 아니신가요?</h5>
                        <ThemeProvider theme={theme}>
                            <Button
                                id="login-btn"
                                component={Link} to="/signup"
                                color="primary"
                                variant="outlined"
                                size="large">
                                회원가입하기
                            </Button>
                        </ThemeProvider>
                    </div>
                    <br />
                </div>
            </Paper >
        </div >
    )
}
export default Login;