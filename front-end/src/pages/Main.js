import React, { useState } from 'react'
import Paper from '@mui/material/Paper';
import Button from '@mui/material/Button';
import { Link } from "react-router-dom";
import { createTheme, ThemeProvider } from '@mui/material/styles';


const Main = ({ token, onLogout }) => {
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

    if (!token) {
        return (
            <Paper elevation={3} id="main-btns-wrap">
                <div id="main-btns-layer">
                    <div id="main-btns">
                        <ThemeProvider theme={theme}>
                            <Button component={Link} to={{
                                pathname: "/login",
                            }} color="primary" variant="contained" size="large">
                                Log in
                            </Button>
                        </ThemeProvider>
                        <ThemeProvider theme={theme}>
                            <Button component={Link} to="/signup" color="primary" variant="contained" size="large" id="sign-up-btn">
                                Sign up
                            </Button>
                        </ThemeProvider>
                    </div>
                </div>
            </Paper>
        )
    }
    else {
        return (
            <Paper elevation={3} id="main-btns-wrap">
                <div id="main-btns-layer">
                    <div id="main-btns">
                        <ThemeProvider theme={theme}>
                            <Button color="primary" variant="contained" size="large" onClick={onLogout}>
                                Log out
                            </Button>
                        </ThemeProvider>
                        <ThemeProvider theme={theme}>
                            <Button component={Link} to="/mypage" color="primary" variant="contained" size="large" id="sign-up-btn">
                                My Page
                            </Button>
                        </ThemeProvider>
                    </div>
                </div>
            </Paper>
        )
    }
}
export default Main