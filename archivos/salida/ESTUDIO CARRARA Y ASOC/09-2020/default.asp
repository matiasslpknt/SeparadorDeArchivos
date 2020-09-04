<!--#include file="easyasp_adovbs.asp"-->
<%
ph=Request("ph")
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

	Cmd.commandtext="Select * from Indice where DB_NRO_PH =" & PH 

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