unit IfDateUtils;

interface

//uses System;
type
  TDaysMethod = (tmACT, tm30ISDA, tm30E, tm30EP, tm30A, tm365, tm30G, tm30BMA);

function LastDayOfPrevMonth(ADate: TDateTime): TDateTime;
function FirstDayOfPrevMonth(ADate: TDateTime): TDateTime;
function FirstDayOfMonth(ADate: TDateTime): TDateTime;
function DaysPerYear(Y: Integer): Integer;
procedure ExtractPeriod(Period: String; var ANumber: Integer; var AUnit: Char);
function IncDatePeriod(ADate: TDateTime; Period: String): TDateTime;
function YearsBetween(const ADate1, ADate2: TDateTime): Integer;
function IsBetween_0229(D1, D2: TDateTime): boolean;
function IsValidDate(Y, M, D: word): Boolean;
function IncDateInterval(ADate: TDateTime; AUnit: char; AInc: integer; AEndM: boolean=false): TDateTime;
procedure ScaleDateUnits(AUNIT: char; AINC: INTEGER; OUT AUNITX: char; OUT AINCX: Integer);
function GetNewYear(Y,M: Word; Ainc: Integer): Word;
function GetNewMonth(M: Word; Ainc: Integer): Word;
function DaysBetweenM(Date1, Date2: TDateTime; Method: TDaysMethod): longint; overload;
function DaysBetweenM(Date1, Date2: TDateTime; Method: char): longint; overload;


implementation

uses SysUtils, JvJCLUtils;

function IsBetween_0229(D1, D2: TDateTime): boolean;
var
  i, y1, y2, m, d: word;
begin
  Result := False;
  DecodeDate(D1, Y1, M, D);
  DecodeDate(D2, Y2, M, D);
  for i := Y1 to Y2 do
  begin
    if IsLeapYear(i) then
    begin
      if (D1 <= (EncodeDate(i, 2, 29))) and (D2 >= (EncodeDate(i, 2, 29))) then
        Result := True;
    end;
  end;
end;


function YearsBetween(const ADate1, ADate2: TDateTime): Integer;
var d: TDateTime;
begin
  result := 0;
  d := ADate1;
  while d < ADate2 do
  begin
    d := IncYear(d,1);
    if d <= ADate2 then
      Inc(Result);
  end;
end;

function LastDayOfPrevMonth(ADate: TDateTime): TDateTime;
var
  Year, Month, Day: Word;
begin
  DecodeDate(ADate, Year, Month, Day);
  Result := EncodeDate(Year,Month,1)-1;
end;

function FirstDayOfMonth(ADate: TDateTime): TDateTime;
var
  Year, Month, Day: Word;
begin
  DecodeDate(ADate, Year, Month, Day);
  Result := EncodeDate(Year,Month,1);
end;

function FirstDayOfPrevMonth(ADate: TDateTime): TDateTime;
var
  Year, Month, Day: Word;
begin
  Result := IncMonth(FirstDayOfMonth(ADate),-1);
end;

function DaysPerYear(Y: Integer): Integer;
begin
  if IsLeapYear(y) then Result := 366
  else Result := 365;
end;

procedure ExtractPeriod(Period: String; var ANumber: Integer; var AUnit: Char);
  var N: Integer; var U: Char;
begin
  N := 1;    //wartosci domyslne
  U := 'D';   //wartosci domyslne
  Period := Trim(Period);
  if Length(Period)<2 then Exit;
  try
    N := StrToInt(Copy(Period,1,length(Period)-1) );
  except
  end;
  U := Period[Length(Period)];
  ANumber := n;
  AUnit := u;
end;

function IncDatePeriod(ADate: TDateTime; Period: String): TDateTime;
var n: Integer; var u: char;
begin
  Result := ADate;
  ExtractPeriod(Period,n,u);
  case u of
  'D': Result := IncDay(ADate,n);
  'W': Result := IncDay(ADate,7*n);
  'M': Result := IncMonth(ADate,n);
  'Q': Result := IncMonth(ADate,3*n);
  'H': Result := IncMonth(ADate,6*n);
  'Y': Result := IncYear(ADate,n);
  end;
end;

function IsValidDate(Y, M, D: word): Boolean;
begin
    Result := (Y >= 1) and (Y <= 9999) and (M >= 1) and (M <= 12) and
    (D >= 1) and (D <= DaysPerMonth(Y, M));
end;

function IncDateInterval(ADate: TDateTime; AUnit: char; AInc: integer; AEndM: boolean=false): TDateTime;
begin
  Result := ADAte;
  case AUnit of
    'Y':
    begin
      if AEndM then
        Result := Result + 1;
      Result := IncDate(Result, 0, 0, AInc);
      if AEndM then
        Result := Result - 1;
    end;
    'Q':
    begin
      if AEndM then
        Result := Result + 1;
      Result := IncDate(Result, 0, AInc * 3, 0);
      if AEndM then
        Result := Result - 1;
    end;
    'M':
    begin
      if AEndM then
        Result := Result + 1;
      Result := IncDate(Result, 0, AInc, 0);
      if AEndM then
        Result := Result - 1;
    end;
    'D': Result := IncDate(Result, AInc, 0, 0);
    'W': Result := IncDate(Result, 7 * AInc, 0, 0);
    else
      raise Exception.Create('Invalid interval');
  end;
end;

procedure ScaleDateUnits(AUNIT: char; AINC: INTEGER; OUT AUNITX: char; OUT AINCX: Integer);
begin
  AUNITX := #0;
  AINCX  := 0;
  IF AUNIT='D' THEN
  BEGIN
     AUNITX := 'D';
     AINCX := AINC;
  END
  ELSE IF AUNIT='W' THEN
  BEGIN
     AUNITX := 'D';
     AINCX  := AINC*7;
  END
  ELSE IF AUNIT='M' THEN
  BEGIN
     AUNITX := 'M';
     AINCX := AINC;
  END
  ELSE IF AUNIT='Q' THEN
  BEGIN
     AUNITX := 'M';
     AINCX := AINC*3;
  END
  ELSE IF AUNIT='H' THEN
  BEGIN
     AUNITX := 'M';
     AINCX := AINC*6;
  END
  ELSE IF AUNIT='Y' THEN
  BEGIN
     AUNITX := 'M';
     AINCX :=AINC*12;
  END;
END;

function GetNewYear(Y,M: Word; Ainc: Integer): Word;
  var x: Integer;
begin
  x:=M;
  x:=X+Ainc;
  if x>0 then
    Result := (Y + ((x-1) div 12))
  else
    Result := (Y -1 + (x div 12));
end;

function GetNewMonth(M: Word; Ainc: Integer): Word;
  var x: Integer;
begin
  x:=M;
  x:=X+Ainc;
  if x>0 then
    Result := (((x-1)mod 12) + 1 )
  else
    Result := (12 + (x mod 12));
end;

function DaysBetweenM(Date1, Date2: TDateTime; Method: TDaysMethod): longint;
var
  Y1, Y2, M1, M2, D1, D2, M: word;
  I: integer;
begin
  Result := 0;
  Date1  := Trunc(Date1);
  Date2  := Trunc(Date2);
  if Date2 - Date1 <= 0 then
    Exit;
  case Method of
    tmAct:
      Result := Trunc(Date2) - Trunc(Date1);
    tm30E:
    begin
      DecodeDate(Date1, Y1, M1, D1);
      DecodeDate(Date2, Y2, M2, D2);
      if D1 = 31 then
        D1 := 30;
      if D2 = 31 then
        D2 := 30;
      Result := (Y2 - Y1) * 360 + (M2 - M1) * 30 + D2 - D1;
    end;
    tm30ISDA:
    begin
      DecodeDate(Date1, Y1, M1, D1);
      DecodeDate(Date2, Y2, M2, D2);
      if D1 = 31 then
        D1 := 30;
      if (D2 = 31) and (D1 = 30) then
        D2 := 30;
      Result := (Y2 - Y1) * 360 + (M2 - M1) * 30 + D2 - D1;
    end;
    tm30BMA:
    begin
      DecodeDate(Date1, Y1, M1, D1);
      DecodeDate(Date2, Y2, M2, D2);
      if ExtractMonth(Date1 + 1) > M1 then
        D1 := 30;
      if (D2 = 31) and (D1 = 30) then
        D2 := 30;
      Result := (Y2 - Y1) * 360 + (M2 - M1) * 30 + D2 - D1;
    end;
    tm30G:
    begin
      DecodeDate(Date1, Y1, M1, D1);
      DecodeDate(Date2, Y2, M2, D2);
      if ExtractMonth(Date1 + 1) > M1 then
        D1 := 30;
      if ExtractMonth(Date2 + 1) > M2 then
        D2 := 30;
      Result := (Y2 - Y1) * 360 + (M2 - M1) * 30 + D2 - D1;
    end;
    tm30A:
    begin
      DecodeDate(Date1, Y1, M1, D1);
      DecodeDate(Date2, Y2, M2, D2);
      if D1 = 31 then
        D1 := 30;
      if D2 = 31 then
      begin
        if D1 < 30 then
        begin
          D2 := 1;
          M2 := M2 + 1;
        end
        else
          D2 := 30;
      end;
      Result := (Y2 - Y1) * 360 + (M2 - M1) * 30 + D2 - D1;
    end;
    tm365:
    begin
      Result := Trunc(Date2) - Trunc(Date1);
      DecodeDate(Date1, Y1, M1, D1);
      DecodeDate(Date2, Y2, M2, D2);
      for i := Y1 to Y2 do
      begin
        if IsLeapYear(i) then
        begin
          if (Date1 < EncodeDate(i, 3, 1)) and (Date2 > EncodeDate(i, 2, 28)) then
            Dec(Result);
        end;
      end;
    end;
  end;
end;

function DaysBetweenM(Date1, Date2: TDateTime; Method: char): longint;
begin
  case Method of
    'A': Result := DaysBetweenM(Date1, Date2, tm30E);
    'B': Result := DaysBetweenM(Date1, Date2, tm30A);
    'C': Result := DaysBetweenM(Date1, Date2, tm365);
    'D': Result := DaysBetweenM(Date1, Date2, tm365);
    'E': Result := DaysBetweenM(Date1, Date2, tmACT);
    'F': Result := DaysBetweenM(Date1, Date2, tmACT);
    'G': Result := DaysBetweenM(Date1, Date2, tmACT);
    'H': Result := DaysBetweenM(Date1, Date2, tm30A);
    'I': Result := DaysBetweenM(Date1, Date2, tm30A);
    'J': Result := DaysBetweenM(Date1, Date2, tm30A);
    'K': Result := DaysBetweenM(Date1, Date2, tm30EP);
    'L': Result := DaysBetweenM(Date1, Date2, tm30ISDA);
    'M': Result := DaysBetweenM(Date1, Date2, tm30G);
    'N': Result := DaysBetweenM(Date1, Date2, tm30BMA);
    'O': Result := DaysBetweenM(Date1, Date2, tmACT);
    else
      Result := 0;
  end;
end;

end.
