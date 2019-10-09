unit IntUtl;

interface

uses SysUtils, DB, Ora,OraObjects, Classes, BusinessDayService, YieldCurveService, IntParam, IntSchedule, IfDateUtils;

  //jezeli kwota ma byc zaokraglona to uzywac DefTimetable.GetInterestAmountR
//function GetInterestAmount(Amount, Rate: extended; Date1, Date2,MaturityDate: TDateTime; Method: char; ACouponInc: Integer; ACouponUnit: String; Eom: Boolean = false): extended;
function GetTypeOfIntRateDesc(t: string): string;
function GetNResetDesc(ART: char): string;
function GetReverseDirection(aIO: char): char;
function GetInterestFactor(Date1, Date2: TDateTime; MaturityDate: TDateTime;
  Method: char; ACouponInc: Integer; ACouponUnit: String; Eom: Boolean; AUserBasisDays : string;
  AUserBasisBasis : integer): extended;
function GetInterestAmount(Amount, Rate: extended; Date1, Date2,MaturityDate: TDateTime;
  Method: char; ACouponInc: Integer; ACouponUnit: String; Eom: Boolean;
  AUserBasisDays : string; AUserBasisBasis : integer;
  FRndRnd: extended; FRndRT: boolean): extended; overload;
function GetInterestAmount(Amount, Rate: extended; Date1, Date2,MaturityDate: TDateTime;
  Method: char; ACouponInc: Integer; ACouponUnit: String; Eom: Boolean;
  AUserBasisDays : string; AUserBasisBasis : integer): extended; overload;

function GetInterestFactorPa(Date1, Date2: TDateTime; MaturityDate: TDateTime;
  Method, PaymentAt: char; Rc, Rr: extended; ACouponInc: Integer; ACouponUnit: String; Eom: Boolean = false;
  AUserBasisDays : string = ''; AUserBasisBasis : integer = 0): extended;

resourcestring
  sTypeOfIntRate_Fixed    = 'Fixed';
  sTypeOfIntRate_Float    = 'Float';
  sTypeOfIntRate_Variable = 'Variable';
  sNRstCaptialization  = 'Capitalization';
  sNRstAutoRepayment = 'Auto repayment';
  sNRstManualRepayment = 'Manual repayment';
  sNRstAmortization = 'Amortization';


//var
//  DefTimetable: TIntTimetable;

implementation

{ TIntTimetable }

uses IfNumUtils, JvJCLUtils, Math;
//IFDbUtils, JvJCLUtils, Dialogs, Math,TextDlg,Variants,IFStrUtils

function GetNResetDesc(ART: char): string;
begin
  case art of
  'C': Result := sNRstCaptialization;
  'P': Result := sNRstAutoRepayment;
  'R': Result := sNRstManualRepayment;
  'A': Result := sNRstAmortization;
  'N': Result := ART;
  else Result := ART;
  end;
end;

function GetTypeOfIntRateDesc(t: string): string;
begin
  if t = '' then
  begin
    Result := '';
    Exit;
  end;
  case t[1] of
    'C': Result := sTypeOfIntRate_Fixed;
    'F': Result := sTypeOfIntRate_Float;
    'V': Result := sTypeOfIntRate_Variable;
  end;
end;

function GetReverseDirection(aIO: char): char;
begin
  if aIO = 'I' then
    Result := 'O'
  else if aIO = 'O' then
    Result := 'I'
  else
    Result := #0;
end;


function GetInterestFactor(Date1, Date2: TDateTime; MaturityDate: TDateTime; Method: char; ACouponInc: Integer; ACouponUnit: String; Eom: Boolean; AUserBasisDays : string;
AUserBasisBasis : integer): extended;
var
  Y1, Y2, M1, M2, D1, D2: word;
  yrs, i, j: integer;
  xm: integer;
  xd1: TDateTime;
  FMTF, direction: Integer;
  N,Fx,C,L,Nx,Cx:Extended;
  is_regular: Boolean;
  Y3, AY, WY, M3, WM, AM, D3, AD: word;
  anchor,target,currc, nextc: TDateTime;
begin
  Date1 := Trunc(date1);
  Date2 := Trunc(date2)+1;
  yrs := 0;
  Result := 0;
  case Method of
    'A': //30/360
      Result := DaysBetweenM(Date1, Date2, tm30E) / 360;
    'B': //30/365
      Result := DaysBetweenM(Date1, Date2, tm30E) / 365;
    'C': //365/360
      Result := DaysBetweenM(Date1, Date2, tm365) / 360;
    'D': //365/365
      Result := DaysBetweenM(Date1, Date2, tm365) / 365;
    'E': //ACT/360
      Result := DaysBetweenM(Date1, Date2, tmACT) / 360;
    'F': //ACT/365
      Result := DaysBetweenM(Date1, Date2, tmACT) / 365;
    'G': //G=ACT/ACT
    begin
      yrs := IfDateUtils.YearsBetween(Date1,Date2);
      if yrs=0 then
      begin
        if IFDateUtils.IsBetween_0229(Date1, Date2) then
          xm := 366
        else
          xm := 365;
        Result := (Date2-Date1)/xm;
      end
      else
      begin
        xd1 := IncYear(Date2,-yrs);
        if IFDateUtils.IsBetween_0229(Date1, xd1) then
          xm := 366
        else
          xm := 365;
        xd1 := IncYear(Date2,-yrs);
        Result := yrs + (xd1-Date1)/xm;
      end;
    end;
    'H'://30A/360
      Result := DaysBetweenM(Date1, Date2, tm30A) / 360;
    'I': //30A/365
      Result := DaysBetweenM(Date1, Date2, tm30A) / 365;
    'J': //ACT/ACT (ISDA)
    begin
      DecodeDate(Date1, Y1, M1, D1);
      DecodeDate(Date2, Y2, M2, D2);
      if Y1=Y2 then
      begin
        Result := (Date2 - Date1) / DaysPerYear(Y1);
      end
      else
      begin
        Result := (EncodeDate(Y1, 12, 31) - Date1 + 1) / DaysPerYear(Y1) +
          (Date2 - EncodeDate(Y2, 01, 01)) / DaysPerYear(Y2) + +Y2 - Y1 - 1;
      end;
    end;
    'K': //30E+/360
      Result := DaysBetweenM(Date1, Date2, tm30EP) / 360;
    'L': //30/360 ISDA
      Result := DaysBetweenM(Date1, Date2, tm30ISDA) / 360;
    'M': //30G/360 GERMAN
      Result := DaysBetweenM(Date1, Date2, tm30G) / 360;
    'N': //30G/360 BMA
      Result := DaysBetweenM(Date1, Date2, tm30BMA) / 360;
    'O': //ACT/ACT
    begin
      if ACouponUnit='' then
        FMTF:=12                     //domyslnie roczny kupon
      else if ACouponUnit='M' then   // FMTF - okres kuponu w miesiacach
        FMTF:=ACouponInc
      else if ACouponUnit='Y' then
        FMTF:=ACouponInc*12
      else if ACouponUnit='Q' then
        FMTF:=ACouponInc*3
      else if ACouponUnit='H' then
        FMTF:=ACouponInc*6
      else
      begin
        Result := 0;
        Exit;
      end;

      N:=Date2 - Date1;
      is_regular:=false;
      L:=12;  //
      Fx:=1;  // ilosc kuponow na rok
      DecodeDate(Date1, Y1, M1, D1);
      DecodeDate(Date2, Y3, M3, D3);
      if FMTF<=12 then //kupon <= roku
      begin
        if (12/FMTF) = (12 div FMTF) then
        begin
          L:=FMTF;
          Fx:=12/FMTF;
          is_regular:=false;
          if ((Y3-Y1)*12+(M3-M1)) = L then
          begin
            if not Eom then //ACT/ACT Normal
            begin
              if D1=D3 then
                is_regular:=true
              else if ((not IsValidDate(Y1,M1,D3)) and (DaysPerMonth(Y1,M1)=D1)) then
                is_regular:=true
              else if ((not IsValidDate(Y3,M3,D1)) and (DaysPerMonth(Y3,M3)=D3)) then
                is_regular:=true;
            end
            else //ACT/ACT Ultimo
            begin
              if ((DaysPerMonth(Y1,M1)=D1) AND (DaysPerMonth(Y3,M3)=D3)) then
                is_regular:=true;
            end;
          end;
        end;
      end;

      if is_regular then   //kupon regularny
      begin
        C:=Date2-Date1;
        Result:= (N/(C*Fx));
        Exit;
      end
      else  //kupon nieregularny
      begin
        Result:=0;
        if Date2 = MaturityDate then //maturity
        begin
          direction:=1;
          anchor:= Date1;
          AY := Y1;
          AM := M1;
          AD := D1;
          target:= Date2;
        end
        else
        begin
          direction:=-1;
          anchor:= Date2;
          AY := Y3;
          AM := M3;
          AD := D3;
          target:= Date1;
        end;

        currc:=anchor;
        i:=0;

        while (direction*(currc-target)<0) do
        begin
          i:= i+direction;
          WY := GetNewYear(AY, AM, Round(i*L));
          WM := GetNewMonth(AM, Round(i*L));
          if not Eom then //ACT/ACT Normal
          begin
            if (not IsValidDate(WY,WM,AD)) then
              nextC:=EncodeDate(WY,WM,DaysPerMonth(WY,WM))
            else
              nextc:=EncodeDate(WY,WM,AD);
          end
          else
            nextc:=EncodeDate(WY,WM,DaysPerMonth(WY,WM));

          Nx:=Min(Date2,Max(nextC,currC)) - Max(Date1,Min(currC,nextC));

          Cx:=direction*(nextC-currC);

          if Nx>0 then
            Result:=Result+(Nx/Cx);

          currC:=nextC;
        end;

        Result:=Result/Fx;
        Exit;
      end;
      Result := 0; //jesli blad
    end;
    'Z' : //definiowana uzytkownika
    begin
      if AUserBasisDays='ACT' then
        Result := DaysBetweenM(Date1, Date2, tmAct)/AUserBasisBasis
      else
        result := StrToInt(AUserBasisDays)/AUserBasisBasis;
    end;
  end;
end;

function GetInterestAmount(Amount, Rate: extended; Date1, Date2,MaturityDate: TDateTime; Method: char; ACouponInc: Integer; ACouponUnit: String; Eom: Boolean; AUserBasisDays : string; AUserBasisBasis : integer; FRndRnd: extended; FRndRT: boolean): extended;
begin
//  Result := Amount*Rate*GetInterestFactor(Date1, Date2,MaturityDate, Method, ACouponInc,ACouponUnit, Eom, AUserBasisDays, AUserBasisBasis)/100;
  //Rate := RoundValP(rate,9);
  Rate := rate + 0.000000000000001; //zaokraglenie do 15 miejsca
  Result := Amount*GetInterestFactor(Date1, Date2,MaturityDate, Method, ACouponInc,ACouponUnit, Eom, AUserBasisDays, AUserBasisBasis);
  result := result * rate;
  result := result /100;
  Result := IfNumUtils.RoundVal(Result,FRndRnd,FRndRT);
end;

function GetInterestAmount(Amount, Rate: extended; Date1, Date2,MaturityDate: TDateTime; Method: char; ACouponInc: Integer; ACouponUnit: String; Eom: Boolean; AUserBasisDays : string; AUserBasisBasis : integer): extended;
begin
//  Result := Amount*Rate*GetInterestFactor(Date1, Date2,MaturityDate, Method, ACouponInc,ACouponUnit, Eom, AUserBasisDays, AUserBasisBasis)/100;
  //Rate := RoundValP(rate,9);
  Rate := rate + 0.000000000000001; //zaokraglenie do 15 miejsca
  Result := Amount*GetInterestFactor(Date1, Date2,MaturityDate, Method, ACouponInc,ACouponUnit, Eom, AUserBasisDays, AUserBasisBasis);
  result := result * rate;
  result := result /100;
  //Result := IfNumUtils.RoundVal(Result,FRndRnd,FRndRT);
end;

 {Oblicza Interest Factor, gdy Method=G lub J wtedy zwraca 0;}
 {Zawsze bierze Rc-Rr * ....}
function GetInterestFactorPa(Date1, Date2: TDateTime; MaturityDate: TDateTime;
  Method, PaymentAt: char; Rc, Rr: extended; ACouponInc: Integer; ACouponUnit: String; Eom: Boolean;
  AUserBasisDays : string; AUserBasisBasis : integer): extended;
begin
  Result := 0;
  if method in ['J', 'G'] then
    exit;
  Result := GetInterestFactor(date1, date2,MaturityDate, method, ACouponInc,ACouponUnit, Eom,
    AUserBasisDays,AUserBasisBasis);
  if PaymentAt = 'E' then      //platnosc na koncu
  begin
    Result := (Rc - Rr) * Result / 100;
  end
  else   //platnosc na poczatku
  begin
    Result := (Rc - Rr) * Result / (100 + Rr * Result);
  end;
end;
end.

