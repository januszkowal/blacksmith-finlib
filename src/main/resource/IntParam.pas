unit IntParam;

interface

uses Generics.Collections, Generics.Defaults, IntTypes;

type
  TInterestParamType = (ipPrimary, ipChg);

  TInterestParamNrm = class
  private
    FNominalUnit: char;
    FNominalInc: Integer;
    fResetDate: TDateTime;
    fIpType: TInterestParamType;
    fAdjRule: char;
    FIntTableId: Integer;
    FFixRate: extended;
    FTypeOfIntRate: Char;
    FBasis: char;
    FUserBasisDays:  string;
    FUserBasisBasis: integer;
    FFixingInc:     integer;
    FFixingUnit:    char;
    FFixingEOM:     boolean;
    FFixingAdjust:  boolean;
    FFixingType:    char;
    FStrikeRate: Extended;
    FCouponInc:     integer;
    FCouponUnit:    char;
    FIntFactor:     extended;
    FIntConst:      extended;
    FIntPeriodId:     integer;
    FFirstCouponDate:   TDateTime;
    FFirstNominalDate: TDateTime;
    FCapitalizationInc: Integer;
    FCapitalizationUnit: char;
  public
    //FStartDateX: TDateTime;
    constructor Create;
    destructor Destroy; override;
    procedure Clear;
    property StrikeRate: Extended read FStrikeRate write FStrikeRate;
    property ResetDate: TDateTime read fResetDate write fResetDate;
    property IpType: TInterestParamType read fipType write fipType;
    property IntTableId: Integer read FIntTableId write FIntTableId;
    property FixRate: Extended read FFixRate write FFixRate;
    property AdjRule: char Read FAdjRule Write FAdjRule;
    property TypeOfIntRate: Char read FTypeOfIntRate write FTypeOfIntRate;
    property FixingType: char Read FFixingType Write FFixingType;
    property FixingInc: integer Read FFixingInc Write FFixingInc;
    property FixingUnit: char Read FFixingUnit Write FFixingUnit;
    property FixingEOM: boolean Read FFixingEOM Write FFixingEOM;
    property FixingAdjust: Boolean read FFixingAdjust write FFixingAdjust;
    property Basis: char read FBasis write FBasis;
    property UserBasisDays: string read FUserBasisDays write FUserBasisDays;
    property UserBasisBasis: integer read FUserBasisBasis write FUserBasisBasis;
    property CouponInc: integer Read FCouponInc Write FCouponInc;
    property CouponUnit: char Read FCouponUnit Write FCouponUnit;
    property IntFactor: extended Read FIntFactor Write FIntFactor;
    property IntConst: extended Read FIntConst Write FIntConst;
    property IntPeriodId: integer Read FIntPeriodId Write FIntPeriodId;
    property FirstCouponDate: TDateTime read FFirstCouponDate write FFirstCouponDate;
    //extra
    property FirstNominalDate: TDateTime read FFirstNominalDate write FFirstNominalDate;
    property NominalInc: Integer read FNominalInc write FNominalInc;
    property NominalUnit: char read FNominalUnit write FNominalUnit;
    property CapitalizationInc: Integer read FCapitalizationInc write FCapitalizationInc;
    property CapitalizationUnit: char read FCapitalizationUnit write FCapitalizationUnit;
  end;

  TInterestParamPrm = class
  private
    FIntTableId: Integer;
    FFixRate: extended;
    FTypeOfIntRate: char;
    FNominal: Extended;
    FBasis:        char;
    FUserBasisDays:  string;
    FUserBasisBasis: integer;
    FIntFactor:    extended;
    FIntConst:     extended;
    FIntPeriodId:    integer;
  public
    constructor Create;
    procedure Clear;
    property IntTableId: Integer read FIntTableId write FIntTableId;
    property FixRate: Extended read FFixRate write FFixRate;
    property TypeOfIntRate: Char read FTypeOfIntRate write FTypeOfIntRate;
    property Nominal: Extended read FNominal write FNominal;
    property Basis: char read FBasis write FBasis;
    property UserBasisDays: string read FUserBasisDays write FUserBasisDays;
    property UserBasisBasis: integer read FUserBasisBasis write FUserBasisBasis;
    property IntFactor: extended read FIntFactor write FIntFactor;
    property IntConst:  extended read FIntConst write FIntConst;
    property IntPeriodId: integer read FIntPeriodId write FIntPeriodId;
  end;

type
  TInterestParams = class
  private
    FIntParamsNrm: TList<TInterestParamNrm>;
    FComparer: IComparer<TInterestParamNrm>;
    FIntParamPrm: TInterestParamPrm;
    FCurrentInterestParamNrm: TInterestParamNrm;
    FCashflowNormalDirection: TCashflowDirection;
    FAlgorithmType: String;
    FYieldCurve: Integer;
    FNegativeInterest: Boolean;
    FIsNegativeInterest: Boolean;
    FMaturityDate:  TDateTime;
    FMaturityDatePmt:  TDateTime;
    FCapFloorFactor: Integer;
    FIsDualAccrual: Boolean;
    FFixingAt:      char;
    FPaymentAt:     char;
    FIsAdjFactor: Boolean;
    FStartDate: TDateTime;
    FStartDateX: TDateTime;
    FIntSchedAfterZeroNominal: Boolean;
    FExistingCoupons: Integer;
    FNominal: extended;
    FNominalStart: Extended;
    FCurrentDate: TDateTime;
    FIsCcyFCS:      boolean;
    FManualRate:    extended;
    FIsEndEventDate: boolean;
    FBaloonnNominal: extended;
    FIsBaloonn: boolean;
    FAmortizationRate: extended;
    FNominalInc: Integer;  //czêstotliwosc i okres splat nominalu
    FNominalUnit: char;
    FFirstNominalDate: TDateTime;  //data pierwszej splaty kapitalu
    FLastNominalDate: TDateTime; //data ostatniej splaty kapitalu
    FCapitalizationInc: Integer;  //czêstotliwosc i okres kapitalizacji odsetek
    FCapitalizationUnit: char;
    FBalloonNominal: Extended;
    function GetCurrentInterestParamNrm: TInterestParamNrm;
    function GetIsRateChangable: Boolean;
    function GetCashflowReverseDirection: TCashflowDirection;
    procedure SetMaturityDate(const Value: TDateTime);
    function GetCurrentIntParIsFixedRate: Boolean;
    function GetCurrentIntParIsFloatRate: Boolean;
    function GetAnyIntParIsFloatRate: Boolean;
  public
    constructor Create;
    destructor Destroy; override;
    procedure Sort;
    procedure Clear;
    function GetBaseIntParamNrm: TInterestParamNrm;
    function CreateIntParamNrmBase(ADate: TDateTime): TInterestParamNrm;
    function CreateIntParamNrm: TInterestParamNrm;
    function SetCurrentInterestParamBase: TInterestParamNrm;
    function SetCurrentInterestParam(ADate: TDateTime): TInterestParamNrm;
    function GetInterestParam(ADate: TDateTime): TInterestParamNrm;
    function GetInterestParamIndex(ADate: TDateTime): Integer;
    property AlgorithmType: String Read FAlgorithmType Write FAlgorithmType;
    property IntParamNrm: TInterestParamNrm read GetCurrentInterestParamNrm;
    property IntParamPrm: TInterestParamPrm read FIntParamPrm;
    property IntParamsNrm: TList<TInterestParamNrm> read FIntParamsNrm;
    property IsRateChangable: Boolean read GetIsRateChangable;
    property CashflowNormalDirection: TCashflowDirection read FCashflowNormalDirection write FCashflowNormalDirection;
    property CashflowReverseDirection: TCashflowDirection read GetCashflowReverseDirection;
    property YieldCurve: Integer read FYieldCurve write FYieldCurve;
    property IsNegativeInterest: Boolean read FIsNegativeInterest write FIsNegativeInterest;
    property MaturityDate:  TDateTime read FMaturityDate write SetMaturityDate;
    property MaturityDatePmt:  TDateTime read FMaturityDatePmt write FMaturityDatePmt;
    property CapFloorFactor: integer Read FCapFloorFactor Write FCapFloorFactor;
    property PaymentAt: char Read FPaymentAt Write FPaymentAt;
    property FixingAt: char Read FFixingAt Write FFixingAt;
    property IsDualAccrual: Boolean read FIsDualAccrual write FIsDualAccrual;
    property IsAdjFactor: Boolean read FIsAdjFactor write FIsAdjFactor;
    property StartDate: TDateTime read FStartDate write FStartDate;
    property StartDateX: TDateTime read FStartDateX write FStartDateX;
    property IntSchedAfterZeroNominal: Boolean read FIntSchedAfterZeroNominal write FIntSchedAfterZeroNominal;

    property Nominal: extended Read FNominal Write FNominal;
    property NominalStart: extended read FNominalStart write FNominalStart;
    property CurrentDate: TDateTime read FCurrentDate write FCurrentDate;
    property IsCcyFCS: boolean Read FIsCcyFCS Write FIsCcyFCS;
    property IsEndEventDate: Boolean read FIsEndEventDate write FIsEndEventDate;

    property CurrentIntParIsFloatRate: Boolean read GetCurrentIntParIsFloatRate;
    property CurrentIntParIsFixedRate: Boolean read GetCurrentIntParIsFixedRate;
    property AnyIntParIsFloatRate: Boolean read GetAnyIntParIsFloatRate;
    property ManualRate: Extended read FManualRate write FManualRate;
    //
    property ExistingCoupons: Integer read FExistingCoupons write FExistingCoupons;
    //Balloon
    property IsBalloon: Boolean read FIsBaloonn write FIsBaloonn;
    property BalloonNominal: Extended read FBalloonNominal write FBalloonNominal;
    property AmortizationRate: Extended read FAmortizationRate write FAmortizationRate;
    property LastNominalDate: TDateTime read FLastNominalDate write FLastNominalDate;
  end;

implementation

uses SysUtils;

{ TInterestParamPrm }

constructor TInterestParamPrm.Create;
begin
  Clear;
end;

procedure TInterestParamPrm.Clear;
begin
  FTypeOfIntRate := #0;
  FNominal := 0;
  FixRate  := 0;
  FUserBasisDays := '';
  FUserBasisBasis := 0;
end;


{ TInterestParamNrm }
procedure TInterestParamNrm.Clear;
begin
  FFixRate := 0;
end;

constructor TInterestParamNrm.Create;
begin
  FFixRate  := 0;
  FUserBasisDays := '';
  FUserBasisBasis := 0;
  FFixingAdjust := False;
end;

destructor TInterestParamNrm.Destroy;
begin

  inherited;
end;

constructor TInterestParams.Create;
begin
  FComparer := TDelegatedComparer<TInterestParamNrm>.Create (
    function(const Left, Right: TInterestParamNrm): Integer
    begin
      Result := 0;
      if (Left.ResetDate < Right.ResetDate) then
        Result := -1
      else if (Left.ResetDate > Right.ResetDate) then
        Result := 1;
    end);
  FIntParamsNrm := TList<TInterestParamNrm>.Create(FComparer);
  FIntparamPrm := TInterestParamPrm.Create;
  FCurrentInterestParamNrm := nil;
  FAlgorithmType := 'NRM';
  FNegativeInterest := False;
  FIsDualAccrual := False;
  FIsAdjFactor:= False;
  FPaymentAt := 'E';
  FFixingAt := 'B';
  FIntSchedAfterZeroNominal := False;
  FExistingCoupons:=0;
  FIsEndEventDate := False;
  FStartDateX := 0;
  FCashflowNormalDirection := csNone;
end;

destructor TInterestParams.Destroy;
begin
  Clear;
  FIntParamsNrm.Free;
  FIntparamPrm.Free;
end;

procedure TInterestParams.Clear;
var
IP: TInterestParamNrm;
begin
  FIntparamPrm.Clear;
  FStartDateX := 0;
  FNominal := 0;
  FFirstNominalDate := 0;
  FLastNominalDate := 0;
  FIsEndEventDate := False;
  FCapitalizationInc:=0;
  FCapitalizationUnit:=#0;
  FStartDateX := 0;
  FCurrentInterestParamNrm := nil;
  while FIntParamsNrm.Count > 0 do
  begin
    IP := FIntParamsNrm.Items[0];
    IP.Free;
    FIntParamsNrm.Delete(0);
  end;
end;

function TInterestParams.CreateIntParamNrmBase(ADate: TDateTime): TInterestParamNrm;
begin
  //if FIntParamsNrm.Count > 0 then
  //  raise Exception.Create('Interest Params must be empty');
  Result := CreateIntParamNrm;
  Result.fipType := ipPrimary;
  Result.fResetDate := ADate;
  FCurrentInterestParamNrm := Result;
end;

function TInterestParams.GetBaseIntParamNrm: TInterestParamNrm;
var ip: TInterestParamNrm;
i: Integer;
begin
  for i := 0 to FIntParamsNrm.Count - 1 do
  begin
    if FIntParamsNrm[0].fipType=ipPrimary then
    begin
      Result := FIntParamsNrm[0];
      Exit;
    end;
  end;
end;

function TInterestParams.GetCashflowReverseDirection: TCashflowDirection;
begin
  if FCashflowNormalDirection=csIn then
    Result := csOut
  else if FCashflowNormalDirection=csOut then
    Result := csIn
  else
    Result := csNone;
end;

function TInterestParams.GetCurrentInterestParamNrm: TInterestParamNrm;
begin
  if FCurrentInterestParamNrm=nil then
    raise Exception.Create('Current Interest Param is not set');
  Result := FCurrentInterestParamNrm;
end;

function TInterestParams.GetInterestParam(ADate: TDateTime): TInterestParamNrm;
var i: Integer;
begin
  Result := nil;
  i:= GetInterestParamIndex(ADate);
  if i=-1 then Exit;
  Result := FIntParamsNrm[i];
end;

function TInterestParams.GetInterestParamIndex(ADate: TDateTime): Integer;
var i: Integer;
ibase: Integer;
begin
  Result := -1;
  FIntParamsNrm.Sort;
  for i := 0 to FIntParamsNrm.Count - 1 do
  begin
    if FIntParamsNrm[i].ResetDate<=ADate then
    begin
      Result := i;
      Break;
    end;
  end;
end;

function TInterestParams.GetIsRateChangable: Boolean;
var i: Integer;
begin
  Result := (FIntParamPrm.TypeOfIntRate<>#0);
  if not Result then
  begin
    for I := 0 to FIntParamsNrm.Count - 1 do
    begin
      if FIntParamsNrm[i].TypeOfIntRate='F' then
      begin
        Result := True;
        Break;
      end;
    end;
  end;
end;

function TInterestParams.SetCurrentInterestParamBase: TInterestParamNrm;
begin
  FCurrentInterestParamNrm := GetBaseIntParamNrm;
  Result := FCurrentInterestParamNrm;
end;

procedure TInterestParams.SetMaturityDate(const Value: TDateTime);
begin
  FMaturityDate := Value;
  FMaturityDatePmt := Value;
end;

procedure TInterestParams.Sort;
begin
  FIntParamsNrm.Sort;
end;

function TInterestParams.SetCurrentInterestParam(ADate: TDateTime): TInterestParamNrm;
begin
  Result := GetInterestParam(ADate);
  FCurrentInterestParamNrm := Result;
  if Result=nil then
    Result := SetCurrentInterestParamBase;
end;

function TInterestParams.CreateIntParamNrm: TInterestParamNrm;
begin
  Result := TInterestParamNrm.Create;
  FIntParamsNrm.Add(Result);
end;

function TInterestParams.GetCurrentIntParIsFixedRate: Boolean;
begin
  Result := (IntParamNrm.TypeOfIntRate='C') or (IntParamPrm.TypeOfIntRate='C');
end;

function TInterestParams.GetCurrentIntParIsFloatRate: Boolean;
begin
  Result := (IntParamNrm.TypeOfIntRate='F') or (IntParamPrm.TypeOfIntRate='F');
end;

function TInterestParams.GetAnyIntParIsFloatRate: Boolean;
var i:Integer;
begin
  Result := (IntParamPrm.TypeOfIntRate='F');
  if Result then Exit;
  for I := 0 to FIntParamsNrm.Count - 1 do
  begin
    if FIntParamsNrm[i].TypeOfIntRate='F' then
    begin
      Result := True;
      Break;
    end;
  end;
end;



end.
