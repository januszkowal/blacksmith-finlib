unit IfNumUtils;

interface
uses Fmtbcd, IfDateUtils;


type
  TRndPar = record
    Rounding: Boolean;
    Decimals: Integer;
  end;
  TRndPar2 = record
    Rounding: Boolean;
    Precision: Extended;
  end;

// Num functions
function FltAdd(P1,P2: Extended; Decimals: Integer): Extended;
function FltDiv(P1,P2: Extended; Decimals: Integer): Extended;
function FltEqual(P1,P2: Extended; Decimals: Integer): Boolean;
function FltEqualZero(P: Extended): Boolean;
function FltGreaterZero(P: Extended): Boolean;
function FltLessZero(P: Extended): Boolean;
function FltNeg(P: Extended; Negate: Boolean): Extended;
function FltMul(P1,P2: Extended; Decimals: Integer): Extended;
function FltRound(P: Extended; Decimals: Integer): Extended;
function FltSub(P1,P2: Extended; Decimals: Integer): Extended;
function FltUnEqualZero(P: Extended): Boolean;
function FltCheckZero(P: Extended): Extended;
function IsMulFlt(I: LongInt; E: Extended): Boolean;
function IntPow10(I: Integer):LongInt;
function RoundVal(Value, Rounding: Extended; RT: Boolean): Extended;
function RoundM(Value: Extended): Extended;
function FloatToStrUS(value: Extended): string; overload;
function FloatToStrUS(value: Extended; decimals: Integer): string; overload;
function StrUsToFloat(value: string) : extended;
function YieldToPrice(Amount, Yield: Extended; D1, D2: TDateTime; YBase : integer=360): Extended;
function PriceToYield(Amount, Price: Extended; PriceType: string; D1, D2: TDateTime; YBase : integer=360): Extended;
function PriceTo100(Amount, Price: Extended; PriceType: string; D1, D2: TDateTime; YBase : integer=360): Extended;
function PriceToDisc(Amount, Price: Extended; PriceType: string; D1, D2: TDateTime; YBase : integer=360): Extended;
function RoundValP(Value: Extended; decimals: Integer): Extended;
function RoundValue(Value: Extended; RndPar: TRndPar): Extended;
function RoundBCD(value: TBCD; decimals: smallint):TBCD;
function TruncBCD(value: TBCD; decimals: smallint):TBCD;
function RoundTruncBCD(value: TBCD; decimals: smallint; RT: Boolean):TBCD;overload;
function RoundTruncBCD(value: TBCD; par: TRndPar):TBCD; overload;
function RoundBCDdbl(value: TBCD; decimals: smallint):Double;
function GetRndParByRounding(opt: string; Rounding: Extended): TRndPar;
function GetRndParByDecimals(opt: string; Decimals: smallint): TRndPar;

implementation

uses SysUtils, Math, Dialogs, JvJCLUtils;

const
	FLTZERO  = 0.000001;
  FLTZERO1 = 0.000000001;

function RoundValue(Value: Extended; RndPar: TRndPar): Extended;
begin
  if rndpar.Rounding then
    Result := FltRound(value,rndpar.Decimals)
  else
    Result := FltRound(value,rndpar.Decimals);
end;

function RoundTruncBCD(value: TBCD; decimals: smallint; RT: Boolean):TBCD; overload;
begin
  if RT then
    Result := RoundBCD(value,decimals)
  else
    Result := TruncBCD(value,decimals);
end;

function RoundTruncBCD(value: TBCD; par: TRndPar):TBCD;
begin
  Result := RoundTruncBCD(value,par.decimals,par.Rounding);
end;

function RoundBCD(value: TBCD; decimals: SmallInt):TBCD;
var
  s: String;
begin
  s := Fmtbcd.RoundAt(Fmtbcd.BcdToStr(value),decimals);
  Result := Fmtbcd.StrToBcd(s);
end;

function TruncBCD(value: TBCD; decimals: SmallInt):TBCD;
begin
  Result := Fmtbcd.IntegerToBcd(Fmtbcd.BcdToInteger(value,true));
end;

function RoundBCDdbl(value: TBCD; decimals: smallint):Double;
var
  s: String;
  x: TBcd;
begin
  s := Fmtbcd.RoundAt(Fmtbcd.BcdToStr(value),decimals);
  x := Fmtbcd.StrToBcd(s);
  Result := Fmtbcd.BcdToDouble(x);
end;

function RoundVal(Value, Rounding: Extended; RT: Boolean): Extended;
var
  e,y,xe: Extended;
  x: integer;
begin
   if Rounding = 0.0 then Rounding := 1.0;
   if RT then
   begin
     if rounding<1 then
     begin
       x := round(1/rounding);
       if Value < 0 then
         Result := Trunc(Value*x - 0.5)
       else
       begin
         Result := Trunc(Value*x + 0.5);
       end;
       result := result / x;
     end else
     begin
       xe := 1/rounding;
       if Value < 0 then
         Result := Trunc(Value*xe - 0.5)
       else
       begin
         Result := Trunc(Value*xe + 0.5);
       end;
       result := result / xe;
     end;
   end
   else
   begin
     //Result := Trunc(Value/Rounding)*Rounding;
     Result := Trunc(RoundTo(Value/Rounding,-6))*Rounding;  //zmiana ze wzgledu na problem z zaookragleniami
   end;
   Result := FltCheckZero(Result);
end;

function RoundM(Value: Extended): Extended;
begin
   if Value < 0 then
      Result := Trunc(Value - 0.5)
   else
      Result := Trunc(Value + 0.5);
end;

//
function FltGreaterZero(P: Extended): Boolean;
begin
  Result:=P>FLTZERO;
end;

function FltLessZero(P: Extended): Boolean;
begin
  Result := P < -FLTZERO;
end;

function FltEqual(P1,P2: Extended; Decimals: Integer): Boolean;
begin
  P1    :=fltRound(P1,Decimals);
  P2    :=fltRound(P2,Decimals);
  Result:=SameValue(P1,P2,1/IntPower(10,Decimals));
end;

function FltEqualZero(P: Extended): Boolean;
begin
  Result:=(P>=-FLTZERO) and (P<=FLTZERO);
end;

function FltUnEqualZero(P: Extended): Boolean;
begin
  Result:=(P<-FLTZERO) or (P>FLTZERO)
end;

function FltRound(P: Extended; Decimals: Integer): Extended;
var
  //LF: IntLong;
  Factor  : LongInt;
  Tmp    : Extended;
begin
  //Factor:=IntPower(10,Decimals);
  //Factor := IntPow10(decimals);
  //if P<0 then Tmp := -0.5 else Tmp :=0.5;
  //Result:=Int(P*Factor+Tmp)/Factor;
  //Result:=Int(P*Factor+Tmp)/Factor;
  Result := SimpleRoundTo(P,-decimals);
  Result := fltCheckZero(Result);
end;

function FltAdd(P1,P2: Extended; Decimals: Integer): Extended;
begin
  P1    :=fltRound(P1,Decimals);
  P2    :=fltRound(P2,Decimals);
  Result:=fltRound(P1+P2,Decimals);
end;

function FltDiv(P1,P2: Extended; Decimals: Integer): Extended;
begin
  P1:=fltRound(P1,Decimals);
  P2:=fltRound(P2,Decimals);
  if P2=0.0 then P2:=FLTZERO;       { provide division by zero }
  Result:=fltRound(P1/P2,Decimals);
end;

function FltMul(P1,P2: Extended; Decimals: Integer): Extended;
begin
  P1    :=fltRound(P1,Decimals);
  P2    :=fltRound(P2,Decimals);
  Result:=fltRound(P1*P2,Decimals);
end;

function FltSub(P1,P2: Extended; Decimals: Integer): Extended;
begin
  P1    :=fltRound(P1,Decimals);
  P2    :=fltRound(P2,Decimals);
  Result:=fltRound(P1-P2,Decimals);
end;

function FltNeg(P: Extended; Negate: Boolean): Extended;
begin
  if Negate then Result:=-P else Result:=P;
end;

function fltCheckZero(P: Extended): Extended;
begin
  if SameValue(P,0.0,FLTZERO) then
    Result := 0.0
  else
    Result := P;
end;


function IsMulFlt(I: LongInt; E: Extended): Boolean;
var tmp: Extended;
begin
   tmp := E/I;
	Result := FltEqualZero(Frac(tmp));
end;

function IntPow10(I: Integer):LongInt;
begin
   Result := 1;
   for i := 1 to i do Result := Result*10;
end;

function FloatToStrUS(value: Extended; decimals: Integer): string;
var fmt: TFormatSettings;
begin
  GetLocaleFormatSettings(SysLocale.DefaultLCID,fmt);
  fmt.DecimalSeparator := '.';
  fmt.ThousandSeparator := #0;
  CurrencyDecimals := decimals;
  Result := FloatToStr(value,fmt);
end;

function FloatToStrUS(value: Extended): string;
var fmt: TFormatSettings;
begin
  GetLocaleFormatSettings(SysLocale.DefaultLCID,fmt);
  fmt.DecimalSeparator := '.';
  fmt.ThousandSeparator := #0;
  Result := FloatToStr(value,fmt);
end;

function StrUsToFloat(value: string) : extended;
var fmt: TFormatSettings;
begin
  GetLocaleFormatSettings(SysLocale.DefaultLCID,fmt);
  fmt.DecimalSeparator := '.';
  fmt.ThousandSeparator := #0;
  Result := StrToFloat(value,fmt);
end;

function PriceToYield(Amount, Price: Extended; PriceType: string; D1,  D2: TDateTime; YBase : integer=360): Extended;
var xl: Integer;
begin
   Result := 0;
   if YBase=0 then YBase := 360;
   xl := Trunc(D2 - D1);
   if PriceType='P_1' then
   begin
   	if xl <= 0 then
      begin
      	Result := Amount;
         Exit;
      end
      else
      	Result := 100*(YBase*Amount/Price-YBase)/xl;
   end
   else if PriceType = 'P_100' then
   begin
   	if xl <= 0 then
      begin
      	Result := Amount;
         Exit;
      end
      else
      	Result := (100*YBase/Price-YBase)*100/XL;
   end
   else if PriceType = 'DISC' then
   begin
   	if xl <= 0 then
      begin
      	Result := Amount;
         Exit;
      end
      else
      	Result := Price/(100-Price*XL/YBase)*100;
   end
   else Result := Price;
end;

function YieldToPrice(Amount, Yield: Extended; D1,  D2: TDateTime; YBase : integer=360): Extended;
var xl: Integer;
begin
  if YBase=0 then YBase := 360;
	Result := 0;
   xl := Trunc(D2 - D1);
	if xl <=0 then
   begin
   	Result := Amount;
      Exit;
   end;
   Result := Amount*YBase/(Yield*xl/100+YBase);
end;

function PriceTo100(Amount, Price: Extended; PriceType: string; D1, D2: TDateTime; YBase : integer=360): Extended;
var xl: Integer;
begin
  if YBase=0 then YBase := 360;
	Result := 0;
	xl := Trunc(D2 - D1);
  if PriceType='P_1' then
  begin
   	Result := Price*100/Amount;
  end
  else if PriceType = 'P_100' then
  begin
   	Result := Price;
  end
  else if PriceType = 'YIELD' then
  begin
   	Result := 100*YBase/((Price*xl)/100 + YBase);
  end
  else if PriceType = 'DISC' then
  begin
   	Result := 100-Price*XL/YBase;
  end
end;


function PriceToDisc(Amount, Price: Extended; PriceType: string; D1, D2: TDateTime; YBase : integer=360): Extended;
var xl: Integer;
begin
	Result := 0;
	xl := Trunc(D2 - D1);
  if PriceType='P_1' then
  begin
   	Result := 100*(1-price/amount)*YBASE/XL;
  end
  else if PriceType = 'P_100' then
  begin
  	Result := (100-PRICE)*YBASE/XL;
  end
  else if PriceType = 'YIELD' then
  begin
   	Result := 100*Price*YBase/(Price*XL+100*YBase);
  end
  else if PriceType = 'DISC' then
  begin
   	Result := Price;
  end
end;

//places>=0 miejsce po przecinku do jakiego zaokraglamy
//places<0 zaokraglenie czesci calkowitej
function RoundValP(Value: Extended; decimals: Integer): Extended;
var rounding: Integer;
    roundingE: Extended;
begin
  if decimals>=0 then
  begin
    Rounding := Round(IntPower(10,decimals));
    if Value < 0 then
      Result := Trunc(Value*Rounding - 0.5)
    else
      Result := Trunc(Value*Rounding + 0.5);
    Result := Result / rounding;
  end else
  begin
    roundingE := IntPower(10,decimals);
    if Value < 0 then
      Result := Trunc(Value*RoundingE - 0.5)
    else
      Result := Trunc(Value*RoundingE + 0.5);
    Result := Result / RoundingE;
  end;
end;

function GetRndParByRounding(opt: string; Rounding: Extended): TRndPar;
begin
  if opt='' then
    result.rounding := true
  else
    result.rounding := opt='R';
  result.decimals := trunc(-Log10(Rounding) + 0.5);
end;

function GetRndParByDecimals(opt: string; Decimals: smallint): TRndPar;
begin
  if opt='' then
    result.rounding := true
  else
    result.rounding := opt='R';
  result.Decimals := decimals;
end;

end.
