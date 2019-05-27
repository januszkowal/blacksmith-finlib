unit IntSchedule;

interface

uses SysUtils, Generics.Collections, Generics.Defaults, IntTypes;

type
  TInterestRec = class;

  TRateRec = class(TSchedRec)
  private
    FParent: TInterestRec;
    function GetIndex: Integer;
  public
    Rate, Rate1: Extended;
    FixingDate, ResetDate, EndDate: TDateTime;
    Nominal, NominalPrm, Interest, InterestPrm, Amount: Extended;
    FixingType: char;
    ExtAmount, ExtIntRate: Extended;
    constructor Create(AParent: TInterestRec);
    property Parent: TInterestRec read FParent;
    property IndexInParent: Integer read GetIndex;
  end;

  TInterestRec = class(TSchedRec)
  private
    FRateResetsComparer: IComparer<TRateRec>;
    FRateResets: TList<TRateRec>;
    function GetResetItem(Index: Integer): TRateRec;
    function GetResetCount: Integer;
    procedure Sort;
  public
    FixingDate,
    StartDate, EndDate, EventDate, PaymentDate: TDateTime;
    Interest, InterestPrm, BaseAmount, Nominal, NominalPrm, Rate, RatePrm, Amount, NominalChange, AdjFactor: Extended;
    CashflowType: String;
    constructor Create;
    destructor Destroy; override;
    procedure Clear;
    procedure DeleteResetItem(Index: Integer);
    procedure MoveRate(curIndex, newIndex: Integer);
    function GetRateResetByDate(ADate: TDateTime): TRateRec;
    function GetRateResetByDateIndex(ADate: TDateTime): Integer;
    function GetRateResetByDateRangeIndex(ADate: TDateTime): Integer;
    function GetRateResetByDateRange(ADate: TDateTime): TRateRec;
    function AddRateReset: TRateRec; overload;
    function AddRateResetEmpty(AFixDate,AStartDate,AEndDate: TDateTime): TRateRec;
    function AddRateReset(AKey: string;
      ART1: char;
      AFixingDate: TDateTime;
      AResetDate: TDateTime;
      AEndDate: TDateTime;
      //
      ARate: Extended;
      ANominal: Extended;
      AInterest: Extended;
      //
      ARate1: Extended;
      ANominal1: Extended;
      AInterest1: Extended;
      //
      AAmount: Extended;
      AFixingType: char;
      AExtAmount: Extended;
      AExtIntRate: Extended;
      ADone: Boolean): TRateRec; overload;
    property ResetItems[Index: Integer]: TRateRec read GetResetItem;
    property ResetCount: Integer read GetResetCount;
  end;

  TInterestSchedule = class
    FInterestPayments: TList<TInterestRec>;
    FInterestComparer: IComparer<TInterestRec>;
    function GetPaymentsCount: Integer;
    function GetInterestPaymentByKeyIndex(AKey: string): Integer;
    function GetInterestPaymentByKeyDateIndex(ADate: TDateTime): Integer;
    function GetInterestPaymentByDateRangeIndex(ADate: TDateTime): Integer;
    function GetInterestPaymentByStartDateIndex(ADate: TDateTime): Integer;
  public
    constructor Create;
    destructor Destroy;override;
    function AddInterestPayment: TInterestRec; overload;
    //function AddInterestPayment(AKey: string; ART1: Char; AFixingDate, AStartDate, AEndDate, AEventDate: TDateTime): TInterestRec; overload;
    function AddInterestPayment(AKey: string;
      AStartDate, AEndDate, AEventDate, APaymentDate: TDateTime;
      ANominal: Extended;  AAmount: Extended;
      ARate: Extended;
      ACashflowType: char;
      ADone: Boolean): TInterestRec; overload;
    function AddInterestPayment(AKey: string; AStartDate, AEndDate, AEventDate, APaymentDate: TDateTime;
      ANominal, ABaseAmount, AAdjFactor, AAmount,
      ARate: Extended; ACashflowType: char; ADone: Boolean): TInterestRec;overload;
    function AddInterestPaymentEmpty(AStartDate, AEndDate, AEventDate: TDateTime): TInterestRec;
    function AddRateReset(AKey: string;
      ART1: char;
      AFixingDate: TDateTime;
      AResetDate: TDateTime;
      AEndDate: TDateTime;
      //
      ARate: Extended;
      ANominal: Extended;
      AInterest: Extended;
      //
      ARate1: Extended;
      ANominal1: Extended;
      AInterest1: Extended;
      //
      AAmount: Extended;
      AFixingType: char;
      AExtAmount: Extended;
      AExtIntRate: Extended;
      ADone: Boolean): TRateRec; overload;

    procedure Clear;
    procedure Sort;
    procedure AlignRateResets;
    procedure DeleteInterestPayment(index: Integer);
    procedure DeleteInterestPaymentAndAlign(index: Integer);

    function GetInterestPaymentByKey(AKey: string): TInterestRec;
    function GetInterestPaymentByUIDIndex(AUID: string): Integer;
    function GetInterestPaymentByUID(AUID: string): TInterestRec;

    function GetInterestPaymentByKeyDate(ADate: TDateTime): TInterestRec;
    function GetInterestPayment(Index: Integer): TInterestRec;
    function GetInterestPaymentIndex(Pmt: TInterestRec): Integer;

    function GetInterestPaymentByDateRange(ADate: TDateTime): TInterestRec;
    function GetInterestPaymentByStartDate(ADate: TDateTime): TInterestRec;
    function GetInterestPaymentByEndDate(ADate: TDateTime): TInterestRec;
    function GetRateResetByDate(ADate: TDateTime): TRateRec;
    function GetRateResetByDateRange(ADate: TDateTime): TRateRec;
    function GetRateResetByPDate(ADate: TDateTime): TRateRec;
    function GetMaxInterestPayment: TInterestRec;
    function GetMinInterestPayment: TInterestRec;
    function GetMaxInterestPaymentDone: TInterestRec;
    //function GetMaxInterestEndDate1: TDateTime;
    property InterestPaymentsCount: Integer read GetPaymentsCount;
    property InterestPayments[index: Integer]: TInterestRec read GetInterestPayment;
  end;

function GetInterestRecEndDate(InterestRec: TInterestRec): TDateTime;
function GetInterestRecEventDate(InterestRec: TInterestRec): TDateTime;

implementation

resourcestring
    sNoInterestItem = 'No interest item.';//'Brak przep³ywu odsetkowego.'

{TRateRec}

function GetInterestRecEndDate(InterestRec: TInterestRec): TDateTime;
begin
  Result := 0;
  if InterestRec<>nil then
    Result := InterestRec.EndDate;
end;

function GetInterestRecEventDate(InterestRec: TInterestRec): TDateTime;
begin
  Result := 0;
  if InterestRec<>nil then
    Result := InterestRec.EventDate;
end;

constructor TRateRec.Create(AParent: TInterestRec);
begin
  inherited Create;
  FParent := AParent;
  RT1 :='R';
  Rate := 0;
  Rate1 := 0;
  FixingDate := 0;
  ResetDate := 0;
  EndDate := 0;
  Nominal := 0;
  NominalPrm := 0;
  Interest := 0;
  InterestPrm := 0;
  Amount := 0;
  FixingType := 'R';
  ExtAmount := 0;
  ExtIntRate := 0;
end;

function TRateRec.GetIndex: Integer;
begin
  Result := Parent.FRateResets.IndexOf(self);
end;

{TInterestRec}

constructor TInterestRec.Create;
begin
  inherited Create;
  //FArrSched := TList<TRecSched>.Create;
  FRateResetsComparer := TDelegatedComparer<TRateRec>.Create (
    function(const Left, Right: TRateRec): Integer
    begin
      Result := 0;
      if (Left.ResetDate < Right.ResetDate) then
        Result := -1
      else if (Left.ResetDate > Right.ResetDate) then
        Result := 1;
    end);
  FRateResets:= TList<TRateRec>.Create(FRateResetsComparer);
  FixingDate := 0;
  EventDate := 0;
  StartDate := 0;
  EndDate := 0;
  Interest := 0;
  Rate := 0;
  AdjFactor := 1;
  Nominal := 0;
  InterestPrm := 0;
  RatePrm := 0;
  NominalPrm := 0;
  Amount := 0;
  NominalChange := 0;
  CashflowType:='P';
  //FixingType := 'R';
end;

destructor TInterestRec.Destroy;
begin
  Clear;
  FRateResets.Free;
  inherited;
end;

procedure TInterestRec.Clear;
var i: Integer;
R: TRateRec;
begin
  while FRateResets.Count >0 do
  begin
    R := FRateResets.Items[0];
    R.Free;
    FRateResets.delete(0);
  end;
end;

function TInterestRec.GetResetItem(Index: Integer): TRateRec;
begin
  Result := FRateResets.Items[index];
end;

procedure TInterestRec.DeleteResetItem(Index: Integer);
var R: TRateRec;
begin
  r := FRateResets.Items[Index];
  r.Free;
  FRateResets.Delete(index);
end;

procedure TInterestRec.MoveRate(curIndex, newIndex: Integer);
begin
  FRateResets.Move(curIndex,newIndex);
end;

procedure TInterestRec.Sort;
begin
  FRateResets.Sort;
end;

function TInterestRec.GetResetCount: Integer;
begin
  Result := FRateResets.Count;
end;

function TInterestRec.GetRateResetByDate(ADate: TDateTime): TRateRec;
var i: Integer;
begin
  Result := nil;
  i := GetRateResetByDateIndex(ADate);
  if i<>-1 then
    Result := ResetItems[i];
end;

function TInterestRec.GetRateResetByDateIndex(ADate: TDateTime): Integer;
var
  j: Integer;
begin
  Result := -1;
  for j := 0 to ResetCount - 1 do
  begin
    if (ResetItems[j].ResetDate=ADate) then
    begin
      Result := j;
      break;
    end;
  end;
end;

function TInterestRec.GetRateResetByDateRange(ADate: TDateTime): TRateRec;
var i: Integer;
begin
  Result := nil;
  i := GetRateResetByDateRangeIndex(ADate);
  if i<>-1 then
    Result := ResetItems[i];
end;

function TInterestRec.GetRateResetByDateRangeIndex(ADate: TDateTime): Integer;
var
  j: Integer;
begin
  Result := -1;
  for j := 0 to ResetCount - 1 do
  begin
    if (ResetItems[j].ResetDate<=ADate) and ((ResetItems[j].EndDate>=ADate)) then
    begin
      Result := j;
      break;
    end;
  end;
end;

function TInterestRec.AddRateReset: TRateRec;
begin
  Result := TRateRec.Create(Self);
  FRateResets.Add(result);
end;


function TInterestRec.AddRateReset(AKey: string; ART1: char; AFixingDate, AResetDate, AEndDate: TDateTime; ARate, ANominal, AInterest, ARate1, ANominal1,
  AInterest1, AAmount: Extended; AFixingType: char; AExtAmount, AExtIntRate: Extended; ADone: Boolean): TRateRec;
begin
  Result := AddRateReset;
  Result.key := AKey;
  Result.KeyDate := AResetDate;
  //Result.Rt := 'R';
  Result.Rt1 := ART1;
  Result.FixingDate := AFixingDate;
  Result.ResetDate := AResetDate;
  //Result.StartDate := AEventDate;
  //Result.EventDate := AEventDate;
  Result.EndDate := AEndDate;
  //
  Result.Rate := ARate;
  Result.Nominal := ANominal;
  Result.Interest := AInterest;
  Result.Rate1 := ARate1;
  Result.NominalPrm := ANominal1;
  Result.InterestPrm := AInterest1;
  Result.Amount := AAmount;
  Result.isDone := ADone;
  Result.FixingType := AFixingType;
  Result.ExtAmount := AExtAmount;
  Result.ExtIntRate := AExtIntRate;
end;

function TInterestRec.AddRateResetEmpty(AFixDate,AStartDate,AEndDate: TDateTime): TRateRec;
begin
  Result := AddRateReset;
  Result.KeyDate := AStartDate;
  Result.FixingDate := AFixDate;
  Result.ResetDate := AStartDate;
  Result.EndDate := AEndDate;
  //Result.XDate1  := AStartDate;
  //Result.XDate2  := AEndDate;
  Result.isChanged := True;
end;

{TInterestSchedule}
constructor TInterestSchedule.Create;
begin
  FInterestComparer := TDelegatedComparer<TInterestRec>.Create (
    function(const Left, Right: TInterestRec): Integer
    begin
      Result := 0;
      if (Left.EventDate < Right.EventDate) then
        Result := -1
      else if (Left.EventDate > Right.EventDate) then
        Result := 1;
    end);
  FInterestPayments:= TList<TInterestRec>.Create(FInterestComparer);
end;

destructor TInterestSchedule.Destroy;
begin
  FInterestPayments.Clear;
  FInterestPayments.Free;
end;

function TInterestSchedule.AddInterestPayment: TInterestRec;
begin
  Result := TInterestRec.Create;
  Result.RT1 :='I';
  FInterestPayments.Add(Result);
end;

{function TInterestSchedule.AddInterestPayment(AKey: string; ART1: Char; AFixingDate, AStartDate, AEndDate, AEventDate: TDateTime): TInterestRec;
begin
  Result := AddInterestPayment;
  Result.Key := AKey;
  Result.RT1 := ART1;
  Result.FixingDate := AFixingDate;
  Result.StartDate := AStartDate;
  Result.EndDate :=AEndDate;
  Result.EventDate := AEventDate;
  Result.KeyDate := AEventDate;
end;}

function TInterestSchedule.AddInterestPayment(AKey: string; AStartDate, AEndDate, AEventDate, APaymentDate: TDateTime; ANominal, ABaseAmount, AAdjFactor, AAmount,
  ARate: Extended; ACashflowType: char; ADone: Boolean): TInterestRec;
begin
  Result := AddInterestPayment;
  Result.Key := AKey;
  Result.KeyDate := AEventDate;
  //
  Result.FixingDate := AStartDate;
  Result.StartDate := AStartDate;
  Result.EventDate := AEventDate;
  Result.PaymentDate := APaymentDate;
  Result.EndDate := AEndDate;
  //
  Result.Rate := ARate;
  Result.Nominal := ANominal;
  Result.Amount  := AAmount;
  Result.BaseAmount := ABaseAmount;
  Result.AdjFactor := AAdjFactor;
  Result.Interest := AAmount;
  Result.CashflowType := ACashflowType;
  Result.isDone := ADone;
end;

function TInterestSchedule.AddInterestPayment(AKey: string; AStartDate, AEndDate, AEventDate, APaymentDate: TDateTime; ANominal, AAmount, ARate: Extended; ACashflowType: char;  ADone: Boolean): TInterestRec;
begin
  Result := AddInterestPayment;
  Result.Key := AKey;
  Result.KeyDate := AEventDate;
  //
  Result.FixingDate := AStartDate;
  Result.StartDate := AStartDate;
  Result.EventDate := AEventDate;
  Result.PaymentDate := APaymentDate;
  Result.EndDate := AEndDate;
  //
  Result.Rate := ARate;
  Result.Nominal := ANominal;
  Result.Amount  := AAmount;
  Result.BaseAmount := AAmount;
  Result.Interest := AAmount;
  Result.CashflowType := ACashflowType;
  Result.isDone := ADone;
end;

function TInterestSchedule.AddInterestPaymentEmpty(AStartDate, AEndDate, AEventDate: TDateTime): TInterestRec;
begin
  Result := AddInterestPayment;
  Result.Key := '';
  Result.KeyDate := AEventDate;
  //
  Result.FixingDate := AStartDate;
  Result.StartDate := AStartDate;
  Result.EndDate := AEndDate;
  Result.EventDate := AEventDate;
  Result.PaymentDate := AEventDate;
  Result.KeyDate := AEventDate;
  Result.isChanged := True;
end;

procedure TInterestSchedule.Clear;
var
RI: TInterestRec;
begin
  while FInterestPayments.Count > 0 do
  begin
    RI := FInterestPayments.Items[0];
    RI.Free;
    FInterestPayments.Delete(0);
  end;
end;


procedure TInterestSchedule.Sort;
var i: Integer;
begin
  FInterestPayments.Sort;
  for I := 0 to FInterestPayments.Count - 1 do
  begin
    FInterestPayments.Items[i].Sort;
  end;
end;

function TInterestSchedule.GetInterestPaymentByUIDIndex(AUID: string): Integer;
var i: Integer;
begin
  Result := -1;
  for I := 0 to FInterestPayments.Count - 1 do
  begin
    if (FInterestPayments.items[i].UID=AUID) then
    begin
      Result := i;
      break;
    end;
  end;
end;

function TInterestSchedule.GetInterestPaymentByUID(AUID: string): TInterestRec;
var i: Integer;
begin
  Result := nil;
  i := GetInterestPaymentByUIDIndex(AUID);
  if i<>-1 then
    Result := FInterestPayments[i];
end;

function TInterestSchedule.GetInterestPaymentByKeyIndex(AKey: string): Integer;
var i: Integer;
begin
  Result := -1;
  for I := 0 to FInterestPayments.Count - 1 do
  begin
    if (FInterestPayments.items[i].Key=AKey) then
    begin
      Result := i;
      break;
    end;
  end;
end;

function TInterestSchedule.GetInterestPaymentByKey(AKey: string): TInterestRec;
var i: Integer;
begin
  Result := nil;
  i := GetInterestPaymentByKeyIndex(AKey);
  if i<>-1 then
    Result := FInterestPayments[i];
end;

function TInterestSchedule.GetInterestPaymentByKeyDateIndex(ADate: TDateTime): Integer;
var i: Integer;
begin
  Result := -1;
  for I := 0 to FInterestPayments.Count - 1 do
  begin
    if (FInterestPayments.items[i].KeyDate=ADate) then
    begin
      Result := i;
      break;
    end;
  end;
end;

function TInterestSchedule.GetInterestPaymentByKeyDate(ADate: TDateTime): TInterestRec;
var i: Integer;
begin
  Result := nil;
  i := GetInterestPaymentByKeyDateIndex(ADate);
  if i<>-1 then
    Result := FInterestPayments[i];
end;

function TInterestSchedule.GetInterestPaymentByDateRangeIndex(ADate: TDateTime): Integer;
var i: Integer;
begin
  Result := -1;
  for I := 0 to FInterestPayments.Count - 1 do
  begin
    if (FInterestPayments.Items[i].StartDate<=ADate) and
       (FInterestPayments.Items[i].EndDate>=ADate)then
    begin
      Result := i;
      Break;
    end;
  end;
end;

function TInterestSchedule.GetInterestPayment(Index: Integer): TInterestRec;
begin
  Result := FInterestPayments[Index];
end;

function TInterestSchedule.GetInterestPaymentIndex(Pmt: TInterestRec): Integer;
begin
  Result := FInterestPayments.IndexOf(Pmt);
end;

function TInterestSchedule.GetPaymentsCount: Integer;
begin
  Result := FInterestPayments.Count;
end;


function TInterestSchedule.GetInterestPaymentByDateRange(ADate: TDateTime): TInterestRec;
var i: Integer;
begin
  Result := nil;
  i := GetInterestPaymentByDateRangeIndex(ADate);
  if i<>-1 then
    Result := FInterestPayments.Items[i];
end;

function TInterestSchedule.GetInterestPaymentByStartDate(ADate: TDateTime): TInterestRec;
var i: Integer;
begin
  Result := nil;
  i := GetInterestPaymentByStartDateIndex(ADate);
  if i<>-1 then
    Result := FInterestPayments.Items[i];
end;

function TInterestSchedule.GetInterestPaymentByEndDate(ADate: TDateTime): TInterestRec;
var i: Integer;
begin
  Result := nil;
  for I := 0 to FInterestPayments.Count - 1 do
  begin
    if (FInterestPayments.items[i].EndDate=ADate) then
    begin
      Result := FInterestPayments[i];
      break;
    end;
  end;
end;

function TInterestSchedule.GetInterestPaymentByStartDateIndex(ADate: TDateTime): Integer;
var i: Integer;
begin
  Result := -1;
  for I := 0 to FInterestPayments.Count - 1 do
  begin
    if (FInterestPayments.items[i].StartDate=ADate) then
    begin
      Result := i;
      break;
    end;
  end;
end;

procedure TInterestSchedule.AlignRateResets;
var i, j: Integer;
begin
  //ustawienie dat xdate1,xdate2
  for i := 0 to InterestPaymentsCount - 1 do
  begin
    for j := 0 to FInterestPayments[i].ResetCount-1 do
    begin
      //FInterestPayments[i].FRateResets[j].XDate1 := FInterestPayments[i].FRateResets[j].XDate1;
      //FInterestPayments[i].FRateResets[j].XDate2 := FInterestPayments[i].FRateResets[j].XDate2;
    end;
  end;
end;

procedure TInterestSchedule.DeleteInterestPayment(index: Integer);
  var
R: TInterestRec;
begin
  R := FInterestPayments[Index];
  R.Free;
  FInterestPayments.Delete(Index);
end;

procedure TInterestSchedule.DeleteInterestPaymentAndAlign(index: Integer);
var
RDel,RNext: TInterestRec;
begin
  RDel := FInterestPayments[Index];
  if Index < (FInterestPayments.count-1) then
  begin
    RNext := GetInterestPaymentByStartDate(RDel.EndDate+1);
    RNext.StartDate := RDel.StartDate;
    //jeszcze brakuje przesuniecia resetow stopy
  end;
  RDel.Free;
  FInterestPayments.Delete(Index);
end;

function TInterestSchedule.GetRateResetByDate(ADate: TDateTime): TRateRec;
var i, j: Integer;
begin
  Result := nil;
  for I := 0 to InterestPaymentsCount - 1 do
  begin
    for j := 0 to FInterestPayments[i].ResetCount - 1 do
    begin
      if (FInterestPayments[i].FRateResets[j].ResetDate=ADate) then
      begin
        Result := FInterestPayments[i].FRateResets[j];
        break;
      end;
    end;
  end;
end;

function TInterestSchedule.GetRateResetByDateRange(ADate: TDateTime): TRateRec;
var i, j: Integer;
begin
  Result := nil;
  for I := 0 to InterestPaymentsCount - 1 do
  begin
    for j := 0 to FInterestPayments[i].ResetCount - 1 do
    begin
      if (FInterestPayments[i].FRateResets[j].ResetDate<=ADate) and
         (FInterestPayments[i].FRateResets[j].EndDate>=ADate) then
      begin
        Result := FInterestPayments[i].FRateResets[j];
        break;
      end;
    end;
  end;
end;

function TInterestSchedule.GetRateResetByPDate(ADate: TDateTime): TRateRec;
var i, j: Integer;
  xd: TDateTime;
begin
  Result := nil;
  xd := 0;
  for I := 0 to InterestPaymentsCount - 1 do
  begin
    for j := 0 to FInterestPayments[i].ResetCount - 1 do
    begin
      if (FInterestPayments[i].FRateResets[j].ResetDate<=ADate) and
         (FInterestPayments[i].FRateResets[j].ResetDate>xd) then
      begin
          xd := FInterestPayments[i].FRateResets[j].ResetDate;
          Result := FInterestPayments[i].FRateResets[j];
      end;
    end;
  end;
end;

function TInterestSchedule.AddRateReset(
  AKey: string;
  ART1: char;
  AFixingDate: TDateTime;
  AResetDate: TDateTime;
  AEndDate: TDateTime;
  //
  ARate: Extended;
  ANominal: Extended;
  AInterest: Extended;
  //
  ARate1: Extended;
  ANominal1: Extended;
  AInterest1: Extended;
  //
  AAmount: Extended;
  AFixingType: char;
  AExtAmount: Extended;
  AExtIntRate: Extended;
  ADone: Boolean): TRateRec;
var
  FInterestRec: TInterestRec;
begin
  FInterestRec := GetInterestPaymentByDateRange(AResetDate);
  if FInterestRec=nil then
    raise Exception.Create(sNoInterestItem);
  Result := FInterestRec.AddRateReset(AKey, ART1, AFixingDate, AResetDate, AEndDate, ARate, ANominal, AInterest, ARate1, ANominal1,
  AInterest1, AAmount, AFixingType, AExtAmount, AExtIntRate, ADone);
end;

function TInterestSchedule.GetMaxInterestPayment: TInterestRec;
begin
  Result := nil;
  if FInterestPayments.Count=0 then Exit;
  FInterestPayments.Sort;
  Result := FInterestPayments[FInterestPayments.Count-1];
end;

function TInterestSchedule.GetMinInterestPayment: TInterestRec;
begin
  Result := nil;
  if FInterestPayments.Count=0 then Exit;
  FInterestPayments.Sort;
  Result := FInterestPayments[0];
end;

function TInterestSchedule.GetMaxInterestPaymentDone: TInterestRec;
var i: Integer;
begin
  Result := nil;
  if FInterestPayments.Count=0 then Exit;
  for i := FInterestPayments.Count-1 downto 0 do
  begin
    if FInterestPayments[i].isDone then
    begin
      Result := FInterestPayments[i];
      Break;
    end;
  end;
end;


end.
