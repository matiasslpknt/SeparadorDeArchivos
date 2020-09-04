<!--#include file="easyasp_adovbs.asp"-->
<%
ph=Request("ph")
Manzana=Request("Manzana")
Lote=Request("Lote")
Mes=Request("Mes")
Ano=Request("Ano")
consorcio=Request("consorcio")
base=consorcio&".mdb"

set objConn = server.createobject("adodb.connection")
objConn.Open "DRIVER={Microsoft Access Driver (*.mdb)}; DBQ=" & Server.MapPath(base)

Response.Charset="utf-8" 

set Cmd=server.createobject("adodb.command")
Cmd.activeconnection=objConn
Cmd.commandtype=adCmdText

if PH="" then
	if consorcio="LosCerezos" then
		Cmd.commandtext="Select * from Indice where Replace([DB_Unidad],'e','i')='Mz." & Manzana &"Lote" & Lote & "'"
	else
		Lote=right("00"&TRIM(Lote),2)
		Cmd.commandtext="Select * from Indice where Ltrim(DB_Unidad)='Mz. " & Manzana &" Lote " & Lote & "'"
	end if
else
	Cmd.commandtext="Select * from Indice where DB_NRO_PH =" & PH 
end if


set Regs=server.createobject("ADODB.Recordset")
Regs.CursorLocation=adUseClient
Regs.CursorType=adOpenForwardOnly
Regs.open Cmd
if not Regs.Eof then
	link = consorcio&"_"&right("000"&TRIM(regs("DB_Orden")),3)&".pdf"
		response.Redirect(link)
end if
%>

<%
objConn.Close
set objConn=nothing
%>