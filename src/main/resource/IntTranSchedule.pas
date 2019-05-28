unit IntTranSchedule;

interface

uses Generics.Collections, SysUtils, Dialogs,  IntTypes, IntSchedule, IntNominalSchedule, IntParam;

type
  TTranSchedule = class
  private
    FNominalSched: TNominalSchedule;//TList<TNominalRec>;
    FInterestSched: TInterestSchedule;
    FInterestParams: TInterestParams;
    function GetInterestPaymentsCount: Integer;
    function GetNominalResetsCount: Integer;
    function GetInterestPayments: TList<TInterestRec>;
    function GetIntParamsNrm: TList<TInterestParamNrm>;
  public
    constructor Create;
    destructor Destroy; override;
    procedure ClearAll;
    procedure SortSchedule;
    procedure Init;
    function SetCurrentInterestParamByUID(AUID: string):TInterestParamNrm;
    function SetCurrentInterestParamByResetDate(AResetDate: TDateTime): TInterestParamNrm;
    function SetCurrentInterestParamByPaymentStartDate(AStartDate: TDateTime): TInterestParamNrm;
    //
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
    //
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
      ADone: Boolean): TRateRec;
    //function AddNominalReset(AKey: string; ART1: CHAR; AChangeType: char; AResetDate: TDateTime; ANominal: Extended; AChange: Extended; ADone: Boolean): TNominalRec;
    property InterestSched: TInterestSchedule read FInterestSched;
    property NominalSched: TNominalSchedule read FNominalSched;
    property InterestPaymentsCount: Integer read GetInterestPaymentsCount;
    property NominalResetsCount: Integer read GetNominalResetsCount;
    property InterestPayments:  TList<TInterestRec> read GetInterestPayments;
    property InterestParams:  TInterestParams read FInterestParams;
  end;

implementation

uses IfNumUtils;

{ TIntSchedule }

constructor TTranSchedule.Create;
begin
  FInterestSched := TInterestSchedule.Create;
  FNominalSched := TNominalSchedule.Create;
  FInterestParams := TInterestParams.Create;
end;

destructor TTranSchedule.Destroy;
begin
  FNominalSched.Free;
  FInterestSched.Free;
  FInterestParams.Free;
  inherited;
end;


function TTranSchedule.GetIntParamsNrm: TList<TInterestParamNrm>;
begin
  Result := FInterestParams.IntParamsNrm;
end;

function TTranSchedule.GetInterestPayments: TList<TInterestRec>;
begin
  Result :=FInterestSched.FInterestPayments;
end;

function TTranSchedule.GetInterestPaymentsCount: Integer;
begin
  Result := InterestSched.GetPaymentsCount;
end;

function TTranSchedule.GetNominalResetsCount: Integer;
begin
  Result := NominalSched.Count;
end;

procedure TTranSchedule.Init;
begin
  if InterestParams.StartDateX = 0 then
    InterestParams.StartDateX := InterestParams.StartDate;
  if NominalSched.LastNominalDate=0 then
    NominalSched.LastNominalDate := InterestParams.MaturityDate;
end;

{function TTranSchedule.AddInterestPayment(AKey: string; ART1: Char; AFixingDate, AStartDate, AEndDate, AEventDate: TDateTime): TInterestRec;
begin
  Result := InterestSched.AddInterestPayment(AKey,ART1,AFixingDate, AStartDate, AEndDate, AEventDate);
end;}

function TTranSchedule.AddInterestPayment(AKey: string; AStartDate, AEndDate, AEventDate, APaymentDate: TDateTime; ANominal, ABaseAmount, AAdjFactor, AAmount,
  ARate: Extended; ACashflowType: char; ADone: Boolean): TInterestRec;
begin
  Result := InterestSched.AddInterestPayment(AKey, AStartDate, AEndDate, AEventDate, APaymentDate, ANominal, ABaseAmount, AAdjFactor, AAmount,
    ARate, ACashflowType, ADone);
end;

function TTranSchedule.AddRateReset(AKey: string; ART1: char; AFixingDate, AResetDate, AEndDate: TDateTime; ARate, ANominal, AInterest, ARate1, ANominal1,
  AInterest1, AAmount: Extended; AFixingType: char; AExtAmount, AExtIntRate: Extended; ADone: Boolean): TRateRec;
begin
  Result := InterestSched.AddRateReset(AKey, ART1, AFixingDate, AResetDate, AEndDate, ARate, ANominal, AInterest, ARate1, ANominal1,
  AInterest1, AAmount, AFixingType, AExtAmount, AExtIntRate, ADone);
end;

function TTranSchedule.AddInterestPayment(AKey: string; AStartDate, AEndDate, AEventDate, APaymentDate: TDateTime; ANominal, AAmount, ARate: Extended;
  ACashflowType: char; ADone: Boolean): TInterestRec;
begin
  Result := InterestSched.AddInterestPayment(AKey, AStartDate, AEndDate, AEventDate, APaymentDate, ANominal, AAmount, ARate,
    ACashflowType, ADone);
end;

procedure TTranSchedule.ClearAll;
begin
  FNominalSched.Clear;
  FInterestSched.Clear;
  FInterestParams.Clear;
end;

function TTranSchedule.SetCurrentInterestParamByUID(AUID: string): TInterestParamNrm;
var
intRec: TInterestRec;
begin
  intRec := FInterestSched.GetInterestPaymentByUID(auid);
  Result := FInterestParams.SetCurrentInterestParam(intRec.StartDate);
end;

function TTranSchedule.SetCurrentInterestParamByPaymentStartDate(AStartDate: TDateTime): TInterestParamNrm;
begin
  Result := FInterestParams.SetCurrentInterestParam(AStartDate);
  if Result=nil then
    Result := FInterestParams.SetCurrentInterestParamBase
end;

function TTranSchedule.SetCurrentInterestParamByResetDate(AResetDate: TDateTime): TInterestParamNrm;
var
intRec: TInterestRec;
begin
  {ustawia na parametry obowiazujace od poczatku okresu odsetkowego}
  intRec := FInterestSched.GetInterestPaymentByDateRange(AResetDate);
  if intRec=nil then
    Result := FInterestParams.SetCurrentInterestParamBase
  else
    Result := FInterestParams.SetCurrentInterestParam(intRec.StartDate);
end;


procedure TTranSchedule.SortSchedule;
begin
  FInterestParams.Sort;
  FNominalSched.Sort;
  FInterestSched.Sort;
  FInterestSched.AlignRateResets;
end;

end.
