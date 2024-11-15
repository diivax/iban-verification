import * as React from 'react';
import TextField from '@mui/material/TextField';
import { Button, Container, Paper } from '@mui/material';
import Typography from '@mui/joy/Typography';
import { useState } from "react";
import ToggleButton from '@mui/material/ToggleButton';
import ToggleButtonGroup from '@mui/material/ToggleButtonGroup';
import Stack from '@mui/material/Stack';

export default function Iban() {
    const paperStyle = { padding: '50px', margin: '20px auto' };
    const divStyle = { margin: '20px 0 20px 0' };
    const [iban, setIban] = useState('')
    const [data, setData] = useState([])
    const [error, setError] = useState([])
    const [verifyExternal, setVerifyExternal] = React.useState('web');

    const handleChange = (event, newChoice) => {
        setVerifyExternal(newChoice);
    };
    const handleClick = async () => {

        try {
            const data = await fetch(`http://localhost:8080/api/v1/iban/verifyIBAN?iban=${[iban]}&verifyExternal=${[verifyExternal]}`).then(res => res.json()).then(json => {
                console.log(typeof json.message !== 'undefined')
                if (typeof json.message !== 'undefined') {
                    setData({ iban: '', valid: '', bankName: '' })
                    setError(json)
                } else {
                    setData(json)
                }
            })
        } catch (err) {
            console.log(err.message)
            setError(err.json())
        }
    }

    return (
        <Container maxWidth="md">
            {typeof error.message !== 'undefined' && (< Typography
                variant="soft"
                color="danger"
                startDecorator="🚨"
                sx={{ fontSize: 'sm', '--Typography-gap': '0.5rem', p: 1 }}
                margin='20px 0 20px 0'
            >
                {error.message}
            </Typography>)}

            <Paper elevation={3} style={paperStyle}>
                <Typography level='h1' color='primary' gutterBottom  sx={{ fontSize: 'xl' }}>IBAN Validator</Typography>
                <TextField id="outlined-basic" label="IBAN" variant="outlined" fullWidth
                    value={iban} onChange={(e) => setIban(e.target.value)} style={divStyle} />
                <Stack spacing={2}>
                    <item>
                        <ToggleButtonGroup
                            color="primary"
                            value={verifyExternal}
                            exclusive
                            onChange={handleChange}
                            aria-label="Platform"
                        >
                            <ToggleButton value="true">True</ToggleButton>
                            <ToggleButton value="false">False</ToggleButton>
                        </ToggleButtonGroup>
                    </item>
                    <item>
                        <Button variant="contained" onClick={handleClick}>Submit</Button>
                    </item>
                </Stack>
            </Paper>

            <Paper elevation={3} style={paperStyle}>
                <TextField id="outlined-basic" name='iban' label="IBAN" variant="outlined" value={data.iban} fullWidth readonly InputLabelProps={{ shrink: true }} />
                <TextField id="outlined-basic" style={divStyle} name='valid' label="is valid" variant="outlined" value={data.valid} fullWidth readonly InputLabelProps={{ shrink: true }} />
                <TextField id="outlined-basic" name='bank' label="bank name" variant="outlined" value={data.bankName} fullWidth readonly InputLabelProps={{ shrink: true }} />
            </Paper>
        </Container >
    );
}