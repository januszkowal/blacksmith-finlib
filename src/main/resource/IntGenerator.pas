unit IntGenerator;

interface

uses SysUtils, DB, Ora,OraObjects, Classes, BusinessDayService, YieldCurveService, IntParam, IntSchedule, IfDateUtils,
  ScheduleConverter, AdjFactorService, IntTypes, IntTranSchedule, OmniXML, IfInterface;

type

  TNominalFunc = function(ADate: TDateTime): extended of object;

  TIntTimetable = class
  private
    FSession:       TOraSession;
    FTranSchedule: TTranSchedule;
    //FRecSched: PRecSched;
    //Zmiana waluty powoduje  zmiane FRndRnd, FRndRT
    FCcy:           integer;
    FRndRnd:        extended;
    FRndRT:         boolean;
    //FIsRound: Boolean;
     //Czy dozwolone ujemne odsetki  - False-niedozwolone - s¹ zerowane
    //FIntTimetable: TIntTimetable;
    FBusinessDayService: TBusinessDayService;
    FYieldCurveService: TYieldCurveService;
    FAdjFactorService: IAdjFactorService;
    FInterestKey: string;
    FRateResetKey: string;
    FNominalResetKey: string;
    //FPrepared: Boolean;
    procedure SetSession(const Value: TOraSession);
    function GetIsRateOrNominalReset: Boolean;
  private
    FIsRateResetInNominalDate: Boolean;
    FNominalResetDate: TNominalResetDate;
    procedure SetDependentsSession(DB: TOraSession);
    function GetRate(StartDate, EndDate, FixingDate: TDateTime; AIntTable, AIntPeriod: integer): extended;
    function GetRateNrm(StartDate, EndDate, FixingDate: TDateTime): extended;
    function GetRatePrm(StartDate, EndDate, FixingDate: TDateTime): extended;
    function GetAdjFactor(ADate: TDateTime): Extended;
    //function GetYieldFraDD(AYield, Accy: integer; AsOfDate, D1, D2: TDateTime): extended;
    function GetInterestRate(AIntTable, ACcy, AIntPeriod: integer; ARateDate: TDateTime): extended;
    //function GetBusinessDayFC(D: TDateTime; AdjRule: char): TDateTime;
    //function IsBusinessDayFC(ADate: TDateTime): boolean;
    function IntParToXML: IXMLDocument;
    function DelZero: Boolean;
    function DelNonExisingNResets: Boolean;
    function AddRateResetR: Boolean;
    function AddRateResetN: Boolean;
    function UpdateResetN: Boolean;
    procedure Unprepare;
    //procedure InitFCS;
    function GenTimetableInternal(AReset: boolean): integer;
    function GetBusinessDay(D: TDateTime; AdjRule: char; ccy: integer): TDateTime;
    function GetCurrentInterestParamNrm: TInterestParamNrm;
    function GetInterestParamPrm: TInterestParamPrm;
    function GetInterestParams: TInterestParams;
    function GetIsNominalReset: Boolean;
  public
    //ArrHarm: TArrHarm;
    constructor Create(DB: TOraSession);
    destructor Destroy; override;
    procedure ClearSchedule;
    procedure Clear;
    procedure Prepare;
    function SetCurrentInterestParam(ADate: TDateTime): TInterestParamNrm;
    //
    //
    function GetFixingDateFC(ADate: TDateTime): TDateTime;
    function GetNextDayFC(Adate: TDateTime; days: integer): TDateTime;
    procedure GetIntDayFC(ADate: TDateTime; AdjRule: char; AInc: word; AUnit: char; var DateEnd, DateEvent: TDateTime);
    function GenTimetable(AReset: boolean): integer;
    function GetTimetableInternal(AReset: boolean): integer;
    function GenerateCapFloor: integer;
    function Generate: integer;
    procedure SetFCProd(Prod: integer);
    procedure SetFCTran(Tran: integer; art: char);
    procedure SetFCS(AFCS: array of integer);
    // przed wywo³aniem funkcji trzeba ustawic co najmniej walute w celu pobrania zaokraglania
    //mozna tez recznie ustawic zaokraglenie metoda SetRoundD
    function GetInterestAmountNrm(Amount, Rate: extended; Date1, Date2: TDateTime{;
      Method: char; ACouponInc: Integer; ACouponUnit: String; Eom: Boolean;
      AUserBasisDays : string; AUserBasisBasis : integer}): extended;
    function GetInterestAmountPrm(Amount, Rate: extended; Date1, Date2: TDateTime{;
      Method: char; ACouponInc: Integer; ACouponUnit: String; Eom: Boolean;
      AUserBasisDays : string; AUserBasisBasis : integer}): extended;
    procedure SetCcy(accy: Integer);
    //procedure SetRound(ARndRnd: extended; ARndRT: boolean);
    procedure SetRoundD(ADecimals: Integer; ARndRT: boolean);
    procedure SetAmounts;
    procedure SetAmountsCapFloor;

    procedure Show;
    class function CreateObject(DB: TOraSession): TIntTimetable;
    //
    procedure LoadScheduleFromTmpTable;
    function GetNominal(AStartDate, AEndDate: TDateTime): extended; overload;
    function GetNominal(AInterestRec: TInterestRec): extended; overload;
    function GetNominal(AResetRec: TRateRec): extended; overload;
    function UpdateSchedule: Boolean;
    function UpdateSchedule2: Boolean;
    function UpdateScheduleAnnuity: Boolean;
    procedure SaveSchedTmp(AType: char);
    function UpdateDSRate(ADS: TDataSet; Mode: TUpdateMode): Boolean;
    function UpdateDSInt(ADS: TDataSet; Mode: TUpdateMode): Boolean;
    function UpdateDSNominal(ADS: TDataSet): Boolean;
    function RoundValue(AValue: Extended): Extended;
    { Harm - GenTimetable }
    // StartDate, MaturityDate, FirstCoupon, ArrFCS lub IsCcyFCS,
    // PaymentAt, FixingAt, AdjRule, TypeOfIntRate
    // CouponInc, CouponUnit, FixingInc, FixingInc, FixingEom, GReset
    { Harm+Kwoty - Generate}
    // StartDate, MaturityDate, FirstCoupon, ArrFCS lub IsCcyFCS,
    // CouponInc, CouponUnit, FixingInc, FixingInc
    // AdjRule, TypeOfIntRate, Basis
    // Nominal, FixRate
    // IntFactor, IntConst, IntTable, IntPeriod, YieldCurve
    // FixingType, FixingEom
    // SetRound lub SetDefaultRnd
    { CapFloor}
    //data od ktorej ma byc generowane uaktualnienie harmonogramu
    property Ccy: integer Read FCcy Write SetCcy;
    {property ManualRate: extended Read FManualRate Write FManualRate;
    property IsEndEventDate: boolean Read FIsEndEventDate Write FIsEndEventDate;
    property BaloonnNominal: extended Read FBaloonnNominal Write FBaloonnNominal;
    property AmortizationRate: extended Read FAmortizationRate Write FAmortizationRate;
    property IsBaloonn: boolean Read FIsBaloonn Write FIsBaloonn;
    property NominalInc: Integer Read FNominalInc Write FNominalInc;
    property NominalUnit: char Read FNominalUnit Write FNominalUnit;
    property FirstNominalDate: TDateTime Read FFirstNominalDate Write FFirstNominalDate;
    property LastNominalDate: TDateTime read FLastNominalDate write FLastNominalDate;
    property IsFixRate: Boolean read GetIsFixRate;
    property IsFloatRate: Boolean read GetIsFloatRate;
    property IsRateReset: Boolean read GetIsRateReset;
    property IsNominalReset: Boolean read GetIsNominalReset;
    property CapitalizationInc: Integer Read FCapitalizationInc Write FCapitalizationInc;
    property CapitalizationUnit: char Read FCapitalizationUnit Write FCapitalizationUnit;}
    property IsRateOrNominalReset: Boolean read GetIsRateOrNominalReset;
    property Schedule: TTranSchedule read FTranSchedule;
    //
    property InterestKey: string read FInterestKey write FInterestKey;
    property RateResetKey: string read FRateResetKey write FRateResetKey;
    property NominalResetKey: string read FNominalResetKey write FNominalResetKey;
    //
    property BusinessDayService: TBusinessDayService read FBusinessDayService;
    property AdjFactorService: IAdjFactorService read FAdjFactorService write FAdjFactorService;
    property IsRateResetInNominalDate: Boolean read FIsRateResetInNominalDate write FIsRateResetInNominalDate;
    property NominalResetDate: TNominalResetDate read FNominalResetDate write FNominalResetDate;
    //property CurrentInterestParam: TInterestParam read FCurrentInterestParam;
    property TranSchedule: TTranSchedule read FTranSchedule;
    property IntParams: TInterestParams read GetInterestParams;
    property IntParamNrm: TInterestParamNrm read GetCurrentInterestParamNrm;
    property IntParamPrm: TInterestParamPrm read GetInterestParamPrm;
  end;

implementation

uses IfDbUtils, IntUtl, IfNumUtils, Math, IfStrUtils, TextDlg, Variants, BusinessDayConvService, IntNominalSchedule,
  xmlutl, OmniXmlUtils;

procedure TIntTimetable.ClearSchedule;
begin
  Schedule.NominalSched.Clear;
  schedule.InterestSched.Clear;
end;

procedure TIntTimetable.LoadScheduleFromTmpTable;
var xrt,xrt1,xchangeType,xCashflowType,xFixingType: char;
xkey: string;
qryTmp: TOraQuery;
xfixingDate,xresetDate,xstartDate,xendDate,xeventDate: TDateTime;
xrate,xrate1,xinterest,xinterest1,xnominal,xnominal1,xamount,xextAmount,xextIntRate,xnominalChange: Extended;
bDone,bchanged: Boolean;
RR: TRateRec;
RI: TInterestRec;
RN: TNominalRec;
  procedure MakeCommon;
  begin
    xrt1 := (qryTmp.FieldByName('RT1').AsString+'X')[1];
    xkey := qryTmp.FieldByName('KROWID').AsString;
    bChanged := qryTmp.FieldByName('IS_CHANGED').AsInteger=1;
    bDone := qryTmp.FieldByName('IS_DONE').AsInteger=1;
  end;
  procedure MakeNominalReset;
  var ct: char;
  begin
    MakeCommon;
    xchangeType := (qryTmp.FieldByName('CHANGE_TYPE').AsString+'X')[1];
    xnominal := qryTmp.FieldByName('NOMINAL_BASE').AsFloat;
    xnominalChange := qryTmp.FieldByName('NOMINAL_CHANGE').AsFloat;
    xresetDate := qryTmp.FieldByName('START_DATE').AsDateTime;
    RN := Schedule.NominalSched.AddReset(xKey, xRT1, xchangeType, xResetDate, xNominal, xNominalChange, BDone);
    rn.isChanged := bchanged;
  end;
  procedure MakePayment;
  begin
    MakeCommon;
    xFixingDate := qryTmp.FieldByName('FIXING_DATE').AsDateTime;
    xStartDate := qryTmp.FieldByName('START_DATE').AsDateTime;
    xEndDate := qryTmp.FieldByName('END_DATE').AsDateTime;
    xEventDate := qryTmp.FieldByName('EVENT_DATE').AsDateTime;
    xCashflowType := qryTmp.FieldByName('CASHFLOW_TYPE').AsString[1];
    xInterest := qryTmp.FieldByName('INTEREST').AsFloat;
    xRate := qryTmp.FieldByName('RATE').AsFloat;
    xNominal := qryTmp.FieldByName('NOMINAL_BASE').AsFloat;
    //
    xInterest1 := qryTmp.FieldByName('INTEREST1').AsFloat;
    xRate1 := qryTmp.FieldByName('RATE1').AsFloat;
    xNominal1 := qryTmp.FieldByName('NOMINAL_BASE1').AsFloat;
      //
    xAmount := qryTmp.FieldByName('AMOUNT').AsFloat;
    ri := Schedule.InterestSched.AddInterestPayment(xKey, xStartDate, xEndDate, xEventDate, xEventDate, xNominal, xAmount, xRate, xCashflowType, bDone);
    RI.isChanged := bChanged;
  end;
  procedure MakeRateReset;
  begin
    MakeCommon;
    xfixingDate := qryTmp.FieldByName('FIXING_DATE').AsDateTime;
    xresetDate := qryTmp.FieldByName('START_DATE').AsDateTime;//START_DATE=EVENT_DATE
    xendDate := qryTmp.FieldByName('END_DATE').AsDateTime;
    xInterest := qryTmp.FieldByName('INTEREST').AsFloat;
    xRate := qryTmp.FieldByName('RATE').AsFloat;
    xNominal := qryTmp.FieldByName('NOMINAL_BASE').AsFloat;
      //
    xInterest1 := qryTmp.FieldByName('INTEREST1').AsFloat;
    xRate1 := qryTmp.FieldByName('RATE1').AsFloat;
    xNominal1 := qryTmp.FieldByName('NOMINAL_BASE1').AsFloat;
      //
    xAmount := qryTmp.FieldByName('AMOUNT').AsFloat;
    xNominalChange := qryTmp.FieldByName('NOMINAL_CHANGE').AsFloat;
    RR := Schedule.InterestSched.AddRateReset(xkey,xrt1,xfixingDate,xresetDate,xendDate,xrate,xnominal,xinterest,xrate1,xnominal1,xinterest1,xamount,xfixingType,xextAmount,xextIntRate,bdone);
    rr.isChanged := bChanged;
  end;

begin
  //I-odsetki,R-Fixing,N-zmiana kapita³u,P-sp³ata kapita³u
  qryTmp:=TOraQuery.Create(nil);
  qryTmp.Session:=FSession;
  qryTmp.SQL.Clear;
  qryTmp.SQL.Add('SELECT * FROM TMP_TRAN_SCHEDULE ORDER BY DECODE(RT,''I'',1,''P'',2,3)');
  ReopenDS(qryTmp);
  while not qryTmp.Eof do
  begin
    xrt := qryTmp.FieldByName('RT').AsString[1];
    if xrt in ['I','P'] then
      MakePayment
    else if xrt='N' then
      MakeNominalReset
    else if xrt='R' then
      MakeRateReset;
    qryTmp.Next;
  end;
   qryTmp.Close;
   qryTmp.Free;
end;

constructor TIntTimetable.Create(DB: TOraSession);
var
  x: TParam;
begin
  //FPrepared := False;
  FSession := DB;
  FYieldCurveService := TYieldCurveService.Create(DB);
  FBusinessDayService := TBusinessDayService.Create(DB);
  FTranSchedule := TTranSchedule.Create;
  FIsRateResetInNominalDate:= True;
  FNominalResetDate := nrStart;
  FInterestKey := 'ROWID';
  FRateResetKey :=  'ROWID';
  FNominalResetKey := 'ROWID';
end;

class function TIntTimetable.CreateObject(DB: TOraSession): TIntTimetable;
begin
  Result := TIntTimetable.Create(DB);
end;

destructor TIntTimetable.Destroy;
var i: Integer;
begin
  Clear;
  Unprepare;
  FTranSchedule.Free;
  FYieldCurveService.Free;
  FBusinessDayService.Free;
  //FIntParamPrm.Free;
  inherited;
end;

procedure TIntTimetable.SetSession(const Value: TOraSession);
begin
  FSession := Value;
  SetDependentsSession(Value);
end;

function TIntTimetable.DelZero: Boolean;
var
dZeroAM: TDateTime;
i,j: Integer;
b1,b2:  Boolean;
ri: TInterestRec;
rr: TRateRec;
begin
  Result := False;
  if Schedule.InterestPaymentsCount=0 then Exit;
  dZeroAM := Schedule.NominalSched.GetZeroAmountDate;
  if dZeroAM<=0 then Exit;

  Schedule.SortSchedule;
  i := 0;
  while i < Schedule.InterestPaymentsCount do
  begin
    b1 := False;
    ri := Schedule.InterestSched.InterestPayments[i];
    if RI.StartDate >= dZeroAM then
    begin
      b1 := True;
    end
    else if RI.EndDate > dZeroAM then
    begin
      ri.EndDate := dZeroAM-1;
      ri.EventDate := dZeroAM;
      //ri.XDate2  := dZeroAM-1;
      ri.isChanged := True;
      if RI.ResetCount > 0 then
      begin
         j := 0;
         while j < RI.ResetCount do
         begin
           rr := RI.ResetItems[j];
           if rr.ResetDate >= dZeroAM then
           begin
             b2 := True;
           end
           else if RR.EndDate > dZeroAM then
           begin
             RR.EndDate := dZeroAM-1;
             //RR.XDate2  := dZeroAM-1;
             RR.isChanged := True
           end;
           if b2 then
           begin
             RI.DeleteResetItem(j);
             Result := True;
           end
           else Inc(j);
         end;
      end;
    end;
    if b1 then
    begin
      Schedule.InterestSched.DeleteInterestPayment(i);
      Result := True;
    end
    else
    begin
      Inc(i);
    end;
  end;
end;

function TIntTimetable.DelNonExisingNResets: Boolean;
var i,j: Integer;
ri: TInterestRec;
rr: TRateRec;
b1: Boolean;
begin
  Result := False;
//ulozenie resetow
  for I := 0 to Schedule.InterestPaymentsCount - 1 do
  begin
    RI := Schedule.InterestSched.InterestPayments[i];
    if RI.ResetCount=0 then Continue;
    j := 0;
    while j < RI.ResetCount do
    begin
      RR := RI.ResetItems[j];
      if RR.rt1='R' then
        Break;
      if (RR.RT1='N') and (RR.isDone=false) and (Schedule.NominalSched.GetReset(RR.ResetDate)=nil) then
      begin
        if RR.ResetDate=RI.StartDate then
        begin
          RR.RT1 := 'R';
        end
        else
          b1 := True;
      end;
      if b1 then
      begin
        Result := True;
        if j>0 then
        begin
          Schedule.InterestSched.InterestPayments[j-1].EndDate := RR.EndDate;
          Schedule.InterestSched.InterestPayments[j-1].isChanged := True;
        end;
        RI.DeleteResetItem(j);
      end
      else
        Inc(j);
    end;
  end;
end;

function TIntTimetable.AddRateResetR: Boolean;
var i: integer;
RI: TInterestRec;
rr: TRateRec;
xNominal: Extended;
begin
  Result := False;
  if not IsRateOrNominalReset then Exit;
  for I := 0 to Schedule.InterestPaymentsCount - 1 do
  begin
    RI := Schedule.InterestSched.InterestPayments[i];
    if RI.ResetCount=0 then
    begin
      xNominal := GetNominal(Ri);
      RR := RI.AddRateReset('','R',
        ri.StartDate,
        ri.StartDate,
        ri.EndDate,
        //
        IntParamNrm.FixRate,
        xNominal,
        0,
        //
        IntParamPrm.FixRate,
        IntParamPrm.Nominal,
        0,
        0,
        //
        'R',0,0,false);
        //
        RR.isChanged := True;
        //RR.XDate1   := RI.StartDate;
        //RR.XDate2   := RI.EndDate;
        Result := True;
    end;
  end;
end;

function TIntTimetable.AddRateResetN: Boolean;
var i,idxrr:  Integer;
RN: TNominalRec;
RI,RIX: TInterestRec;
RR,RR1,RR2: TRateRec;
dMinIntStart,dMaxEnd: TDate;
begin
  Result := False;
  if not Schedule.NominalSched.IsReset then Exit;
  if not FIsRateResetInNominalDate then Exit;
  RIX := Schedule.InterestSched.GetMaxInterestPaymentDone;
  if RIX=nil then
    dMinIntStart := 0
  else
    dMinIntStart := RIX.EndDate+1;
  dMaxEnd := GetInterestRecEndDate(Schedule.InterestSched.GetMaxInterestPayment);
  for i := 0 to Schedule.NominalSched.Count-1 do
  begin
    RN := Schedule.NominalSched[i];
    //nie mozna robic update zapadlych okresow
    if RN.ResetDate < dMinIntStart then Continue;
    if RN.ResetDate > dMaxEnd then Continue;
    //przerywamy gdy sp³ata do zera
    if (RN.ResetDate>=dMaxEnd) then Break;
    //
    if i < Schedule.NominalSched.Count-1 then
    begin
       if (Schedule.NominalSched[i].ResetDate=Schedule.NominalSched[i+1].ResetDate) then
          Continue;
    end;
    //
    RI := Schedule.InterestSched.GetInterestPaymentByDateRange(RN.ResetDate);
    if RI=nil then
      Continue;
    //
    RR := RI.GetRateResetByDateRange(RN.ResetDate);
    if RR=nil then
    begin
      Continue;
    end
    else
    begin
      if (RI.StartDate=RN.ResetDate) then Continue;
      //if (RR.Rt1='N') and (RR.bDone=false) then
      //begin
        //jesli jest poczatek platnosci odsetkowej w dniu resetu to nie usuwaj typu N
      //  if (RI.StartDate=RN.ResetDate) then Continue;
      //end;
    end;
    //if (rn.Nominal=rr.Nominal) then Continue;
    //podzial
    RR1 := Schedule.InterestSched.AddRateReset('','N',rn.ResetDate,rn.ResetDate,rr.EndDate,
      rr.rate,rr.nominal,0,
      rr.rate1,rr.nominalPrm,0,
      rr.amount,'R',0,0,false);
    //RR1.XDate1 := RR.XDate1;
    //RR1.XDate2 := RR.XDate2;
    RR1.isChanged := True;
    //
    RR.EndDate := RN.ResetDate-1;
    RR.isChanged := True;
    RR1.Parent.MoveRate(RR1.IndexInParent,RR.IndexInParent+1);
    Result := True;
  end;
end;



function TIntTimetable.UpdateResetN: Boolean;
var
bx: Boolean;
begin
  Result := False;
  //usuniecie odsetek po resecie kapita³u do zera
  bx := false;
  if not FTranSchedule.InterestParams.IntSchedAfterZeroNominal then
    bx := DelZero;
  Result := Result or bx;
  bx := DelNonExisingNResets;
  Result := Result or bx;
  bx := AddRateResetR;
  Result := Result or bx;
  bx := AddRateResetN;
  Result := Result or bx;
end;

procedure TIntTimetable.Clear;
begin
  //FPrepared := False;
  FIsRateResetInNominalDate := False;
  FBusinessDayService.Clear;
  FTranSchedule.InterestParams.Clear;
  //FIntParamPrm.Clear;
  ClearSchedule;
end;

function TIntTimetable.IntParToXML: IXMLDocument;
var IntParamNrm: TInterestParamNrm;
//doc: IXMLDocument;
root,ngp, npp, npns, npn: IXMLElement;
ipnrm: TInterestParamNrm;
  procedure AddValueInt(node: IXMLElement; name: string; value: Integer; addzero: Boolean = true);
  begin
    if (value=0) and (not addzero) then Exit;
    SetNodeTextInt(node,name,value);
  end;
  procedure AddValueBool(node: IXMLElement; name: string; value: boolean);
  begin
    //if value='' then Exit;
    SetNodeTextBool(node,name,value);
  end;
  procedure AddValueExtended(node: IXMLElement; name: string; value: Extended);
  begin
    //if value='' then Exit;
    SetNodeTextReal(node,name,value);
  end;
  procedure AddValueString(node: IXMLElement; name: string; value: string; addempty: Boolean = true);
  begin
    if (value='') and (not addempty) then Exit;
    SetNodeTextStr(node,name,value);
  end;
  procedure AddValueChar(node: IXMLElement; name: string; value: char; addempty: Boolean = true);
  begin
    if (value=#0) and (not addempty) then Exit;
    SetNodeTextStr(node,name,value);
  end;
  procedure AddValueDate(node: IXMLElement; name: string; value: TDateTime);
  begin
    if value=0 then Exit;
    SetNodeTextDate(node,name,value);
  end;
begin
  result := CreateXMLDoc;
  SetCodepage(result, 'UTF-8');
  root := result.createElement('doc');
  result.appendchild(root);
  ngp := result.createElement('general');
  root.AppendChild(ngp);
  npp := result.createElement('prm');
  root.AppendChild(npp);
  npns := result.createElement('nrms');
  root.AppendChild(npns);
  for ipnrm in Schedule.InterestParams.IntParamsNrm do
  begin
    npn := result.createElement('nrm');
    npns.AppendChild(npn);
    with ipnrm do
    begin
      AddValueDate(npn,'ResetDate',ResetDate);
      AddValueDate(npn,'FirstCouponDate',FirstCouponDate);
      AddValueString(npn,'AdjRule',AdjRule);
      AddValueString(npn,'Basis',Basis);
      AddValueChar(npn,'TypeOfIntRate',TypeOfIntRate);
      AddValueExtended(npn,'FixRate',FixRate);
      AddValueInt(npn,'IntTableId',IntTableId,false);
      AddValueInt(npn,'IntPeriodId',IntPeriodId,false);
      AddValueExtended(npn,'IntFactor',IntFactor);
      AddValueExtended(npn,'IntConst',IntConst);
      AddValueInt(npn,'CouponInc',CouponInc,false);
      AddValueChar(npn,'CouponUnit',CouponUnit,false);
      AddValueInt(npn,'FixingInc',FixingInc,false);
      AddValueChar(npn,'FixingUnit',FixingUnit,false);
      AddValueBool(npn,'FixingEOM',FixingEOM);
      AddValueChar(npn,'FixingType',FixingType);
      AddValueBool(npn,'FixingAdjust',FixingAdjust);
      AddValueInt(npN,'NominalInc',NominalInc,false);
      AddValueChar(npN,'NominalUnit',NominalUnit,false);
      AddValueString(npn,'UserBasisDays',UserBasisDays,false);
      AddValueInt(npn,'UserBasisBasis',UserBasisBasis,false);
      AddValueInt(npN,'CapitalizationInc',CapitalizationInc,false);
      AddValueChar(npN,'CapitalizationUnit',CapitalizationUnit,false);
      AddValueDate(npN,'FirstNominalDate',FirstNominalDate);
    end;
  end;
  //general params
  //AddValueInt(ngp,'TransactionId',Schedule.InterestParams);
  //AddValueString(ngp,'Rt',Schedule.InterestParams);
  with Schedule.InterestParams do
  begin
    AddValueString(ngp,'AlgorithmType',AlgorithmType);
    AddValueExtended(ngp,'Nominal',Nominal);
    AddValueExtended(ngp,'NominalStart',NominalStart);
    AddValueInt(ngp,'YieldCurveId',YieldCurve,false);
    AddValueChar(ngp,'PaymentAt',PaymentAt);
    AddValueChar(ngp,'FixingAt',FixingAt);
    AddValueDate(ngp,'CurrentDate',CurrentDate);
    AddValueDate(ngp,'StartDate',StartDate);
    AddValueDate(ngp,'StartDateX',StartDateX);
    AddValueDate(ngp,'MaturityDate',MaturityDate);
    //
    AddValueInt(ngp,'YieldCurveId',YieldCurve,false);
    AddValueInt(ngp,'CcyId',FCcy);
    AddValueBool(ngp,'IsNegativeInterest',IsNegativeInterest);
    AddValueChar(ngp,'IoPosDir',CashflowDirectionToChar(CashflowNormalDirection));
    AddValueInt(ngp,'ExistingCoupons',ExistingCoupons);
    AddValueBool(ngp,'IntSchedAfterZeroNominal',IntSchedAfterZeroNominal);
    AddValueBool(ngp,'IsEndEventDate',IsEndEventDate);
    AddValueBool(ngp,'IsBalloon',IsBalloon);
    AddValueExtended(ngp,'BalloonNominal',BalloonNominal);
    AddValueExtended(ngp,'AmortizationRate',AmortizationRate);
    AddValueDate(ngp,'LastNominalDate',LastNominalDate);
  end;
  // prm

  if Schedule.InterestParams.IsDualAccrual then
  begin
    with Schedule.InterestParams.IntParamPrm do
    begin
      AddValueExtended(npp,'Nominal',Nominal);
        AddValueString(npp,'Basis',Basis);
        AddValueChar(npp,'TypeOfIntRate',TypeOfIntRate);
        AddValueExtended(npp,'FixRate',FixRate);
        AddValueInt(npp,'IntTableId',IntTableId,false);
        AddValueInt(npp,'IntPeriodId',IntPeriodId,false);
        AddValueExtended(npp,'IntFactor',IntFactor);
        AddValueExtended(npp,'IntConst',IntConst);
        AddValueString(npp,'UserBasisDays',UserBasisDays);
        AddValueInt(npp,'UserBasisBasis',UserBasisBasis,false);
    end;
  end;
  {IntParamNrm := CurrentIntParamNrm;
  //INICJACJA REKORDU ORACLE
  IntDBObject.AttrAsFloat['FRndRnd']:=FRndRnd;
  IntDBObject.AttrAsInteger['FRndRT']:=IfDBUtils.Bool2Int(FRndRT);
  IntDBObject.AttrAsFloat['FBaloonnNominal']:=FBaloonnNominal; }
end;

function TIntTimetable.Generate: integer;
var
  i: integer;
  //IntType: TOraType;
  //IntObject :TOraObject;
  spProc: TOraStoredProc;
  dZeroAM: TDateTime;
  iparxml: IXMLDocument;
  ms: TMemoryStream;
begin
  Prepare;
  //FCurrentInterestParam := schedule.InterestParams.GetBaseIntParamx();
  Result:=0;
  dZeroAM := 0;
  Schedule.SortSchedule;
  if Schedule.InterestPaymentsCount>0 then
  begin
    if not Schedule.InterestParams.IntSchedAfterZeroNominal then
    begin
      dZeroAM := Schedule.NominalSched.GetZeroAmountDate;
      if dZeroAM>0 then
        Schedule.InterestParams.MaturityDate := dZeroAM;
    end;
  end;
  Schedule.Init;

  if IntParams.AlgorithmType='NRM' then
  begin
    Result := GenTimetableInternal(Schedule.InterestParams.IsRateChangable);
    if Schedule.InterestPaymentsCount = 0 then
      Exit;
    UpdateResetN;
    SetAmounts;
  end else
  begin
    SaveSchedTmp('N');
    ClearSchedule;
    //IntType:=TOraType.Create(FSession.OCISvcCtx,FSession.Schema+'.INT_TIMETABLE');  //!!!iforce1
    //IntObject := TOraObject.Create(IntType);
    //IntType.Free;
    iparxml := IntParTOXML;
    //ms := TMemoryStream.Create;
    //XMLSaveToStream(iparxml,ms,ofIndent);
    ilog.AddLog('InterestParameters:' + XMLSaveToString(iparxml));
    spProc:=TOraStoredProc.Create(nil);
    spProc.Session:=FSession;
    spProc.StoredProcName:='INT_SCHED_UTL.GENERATE';
    spProc.Prepare;
    spProc.Params[1].AsString:= XMLSaveToString(iparxml);
    spProc.Execute;

    if not spProc.ParamByName('RESULT').IsNull then
    begin
      Result:=spProc.ParamByName('RESULT').AsInteger;
    end;
    spProc.UnPrepare;
    spProc.Free;
    //ms.Free;

    if Result = 0 then
      Exit;
    LoadScheduleFromTmpTable;
    Result:=Schedule.InterestPaymentsCount;
  end;
end;

function TIntTimetable.GenTimetable(AReset: boolean): integer;
begin
  Prepare;
  Result := GenTimetableInternal(AReset);
end;

function TIntTimetable.GenTimetableInternal(AReset: boolean): integer;
var
  iter: integer;
  dPmtStart, dPmtEnd, dPmtEvent, dCleanEvent, dCleanEventOut, dPmtPmt, dFix, dX: TDateTime;
  bpol: IBusinessDayPolicy;
  bdcs: TBusinessDayConvService;
  procedure AddPayment;
  var
    i, j: integer;
    dRstStart, dRstEnd: TDateTime;
    FInterestRec: TInterestRec;
    FRateRec: TRateRec;
  begin
    if Schedule.InterestParams.IsEndEventDate then
      dPmtEnd := dPmtEvent;
    if Result = 0 then
    begin
      if Schedule.InterestParams.StartDateX > dPmtStart then
        Exit;
      if Schedule.InterestParams.StartDateX < dPmtStart then
        dPmtStart := Schedule.InterestParams.StartDateX;
    end;
    Inc(Result);
    FInterestRec := Schedule.InterestSched.AddInterestPaymentEmpty(dPmtStart, dPmtEnd, dPmtEvent);
    if AReset then
    begin
      if (IntParamNrm.FixingInc = 0) or ((IntParamNrm.FixingInc = IntParamNrm.CouponInc) and
          (IntParamNrm.FixingUnit = IntParamNrm.CouponUnit)) then
      begin
        if Schedule.InterestParams.FixingAt = 'B' then
          dFix := GetFixingDateFc(dPmtStart)   //dRstStart
        else
          dFix := GetFixingDateFc(dPmtEnd);     //dRstEnd
        FRateRec := FInterestRec.AddRateResetEmpty(dFix,dPmtStart,dPmtEnd);
      end
      else
      begin
        j := 1;
        dRstStart := dPmtStart;
        while dRstStart <= dPmtEnd do
        begin
          if Schedule.InterestParams.FixingAt = 'B' then
            dFix := GetFixingDateFc(dRstStart)
          else
            dFix := GetFixingDateFc(dRstEnd);
          dRstEnd := IncDateInterval(dPmtStart, IntParamNrm.FixingUnit, IntParamNrm.FixingInc * j, IntParamNrm.FixingEOM) - 1;
          if dRstEnd > dPmtEnd then
            dRstEnd := dPmtEnd;
          FRateRec := FInterestRec.AddRateResetEmpty(dFix,dRstStart,dRstEnd);
          //FRateRec.XDate1  := dPmtStart;
          //FRateRec.XDate2  := dPmtEnd;
          dRstStart := dRstEnd + 1;
          Inc(j);
        end;
      end;
    end;
  end;
begin
  Result := 0;

  try
    bpol := TBusinessDayPolicyGeneral.Create(FBusinessDayService);
    bdcs := TBusinessDayConvService.Create(bpol, Schedule.InterestParams.MaturityDate);
    if Schedule.InterestParams.PaymentAt = 'B' then
    begin
      SetCurrentInterestParam(Schedule.InterestParams.StartDate);
      if (IntParamNrm.FirstCouponDate >= Schedule.InterestParams.MaturityDate) and (Schedule.InterestParams.PaymentAt = 'B') then
        exit;
      iter := 1;
      dPmtStart := IntParamNrm.FirstCouponDate;
      dX := dPmtStart;
      while dPmtStart < Schedule.InterestParams.MaturityDate do
      begin
        dPmtEnd := IncDateInterval(dX, IntParamNrm.CouponUnit, IntParamNrm.CouponInc * iter, IntParamNrm.FixingEOM) - 1;
        if (iter = 1) then
          dPmtEvent := dPmtStart
        else
          dPmtEvent := FBusinessDayService.GetNextDay(dPmtStart - 1, 1);
        dFix := GetFixingDateFc(dPmtStart);
        if dPmtEnd >= Schedule.InterestParams.MaturityDate then
          dPmtEnd := Schedule.InterestParams.MaturityDate - 1;
        if dPmtEnd>=dPmtStart then  //end nie mo¿e byæ mniejsze od start; jesli tak jest to kupon trzeba rozszerzyc o kolejny okres
        begin
          //if Schedule.InterestParams.IsEndEventDate then dPmtEnd := dPmtEvent;
          AddPayment;
          dPmtStart := dPmtEnd + 1;
        end;
        Inc(iter);
      end;
    end
    else
    begin
      SetCurrentInterestParam(Schedule.InterestParams.StartDate);
      dPmtStart := Schedule.InterestParams.StartDate;
      if (IntParamNrm.FirstCouponDate = Schedule.InterestParams.MaturityDate) or (IntParamNrm.CouponInc = 0) then
      begin
        dPmtEnd := Schedule.InterestParams.MaturityDate - 1;
        dPmtEvent := Schedule.InterestParams.MaturityDate;
        //czy potrzebne
        if Schedule.InterestParams.FixingAt = 'B' then
          dFix := GetFixingDateFc(dPmtStart)
        else
          dFix := GetFixingDateFc(dPmtEnd);
        //if Schedule.InterestParams.IsEndEventDate then dPmtEnd := dPmtEvent;
        AddPayment;
        Exit;
      end
      else if IntParamNrm.FirstCouponDate <= Schedule.InterestParams.StartDate then
      begin
        Iter := 1;
      end
      else   //dFirst > dStart
      begin
        Iter := 0;
      end;
      dCleanEvent :=0;
      while dPmtStart < Schedule.InterestParams.MaturityDate do
      begin
        SetCurrentInterestParam(dPmtStart);
        dX := IntParamNrm.FirstCouponDate;
        //bdcs.GetIntDay(dX, cip.AdjRule, iter * IntParamNrm.CouponInc, IntParamNrm.CouponUnit, IntParamNrm.FixingEom, IntParams.IsEndEventDate, dPmtEnd, dPmtEvent);
        bdcs.GetIntDay2(dX, dPmtStart, IntParamNrm.AdjRule, IntParamNrm.CouponInc, IntParamNrm.CouponUnit, IntParamNrm.FixingEom, IntParams.IsEndEventDate, dCleanEvent, dPmtEnd, dPmtEvent, dCleanEventOut);
        dCleanEvent:=dCleanEventOut;
        if (dPmtEvent >= Schedule.InterestParams.MaturityDate) then
        begin
          dPmtEvent := Schedule.InterestParams.MaturityDate;
          dPmtEnd := Schedule.InterestParams.MaturityDate - 1;
        end;
        //if dPmtEnd>=dPmtStart then  //end nie mo¿e byæ mniejsze od start; jesli tak jest to kupon trzeba rozszerzyc o kolejny okres
        //begin
        AddPayment;
        dPmtStart := dPmtEnd + 1;
        //end;
        Inc(iter);
      end;
    end;
  finally
    bdcs.Free;
    //bpol.Free;
  end;
end;

function TIntTimetable.GenerateCapFloor: integer;
var
  i: integer;
  xRate: extended;
  FInterestRec: TInterestRec;
  cip: TInterestParamNrm;
begin
  Prepare;
  Schedule.Init;
  //InitFCS;
  cip := Schedule.InterestParams.SetCurrentInterestParamBase;
  Result := GenTimetableInternal(False);
  if Result = 0 then
    Exit;
  UpdateResetN;
  SetAmountsCapFloor;
  {for i := 0 to Schedule.InterestPaymentsCount-1 do
  begin
    xRate := cip.FixRate;
    FInterestRec := Schedule.InterestSched.InterestPayments[i];
    if not (IntParamNrm.FixingType in ['M','Z']) then
    begin
      //FRecSched := FArrSched[i];
      if IntParams.YieldCurve > 0 then
      begin
        if FInterestRec.StartDate <= Schedule.InterestParams.CurrentDate then
          xRate := GetInterestRate(cip.IntTableId, Fccy, IntParamNrm.IntPeriodId, FInterestRec.FixingDate)
        else
          xRate := FYieldCurveService.GetYieldFRADD(IntParams.YieldCurve, Fccy, Schedule.InterestParams.CurrentDate, FInterestRec.StartDate,
            FInterestRec.EndDate + 1);
        //xRate := xRate * FCapFloorFactor;
      end
      else
      begin
        if cip.IntTableId > 0 then
        begin
          xRate := GetInterestRate(cip.IntTableId, Fccy, IntParamNrm.IntPeriodId, FInterestRec.FixingDate);
        end;
        //xRate := xRate * FCapFloorFactor;
      end;
    end else
    begin
      xRate := Schedule.InterestParams.ManualRate;
    end;
    //xRate := xRate*InterestFactor + InterestConstant;

    FInterestRec.Rate := xRate;
    if FTranSchedule.InterestParams.CapFloorFactor = -1 then //transakcja FLOOR
    begin
      if cip.FixRate < xRate then
        xRate := Max(xRate - cip.FixRate,0);
    end
    else      //transakcja CAP
    begin
      if cip.FixRate > xRate then
        xRate := Max(cip.FixRate - xRate,0);
    end;
    if xRate <= cip.FixRate then
    begin
      FInterestRec.Interest := 0;
      FInterestRec.InterestPrm := 0;
      FInterestRec.Amount := 0;
    end
    else
    begin
      FInterestRec.Interest := GetInterestFactorPa(FInterestRec.StartDate,
        FInterestRec.EndDate, Schedule.InterestParams.MaturityDate, IntParamNrm.Basis, Schedule.InterestParams.PaymentAt, cip.FixRate, xRate,
        IntParamNrm.CouponInc, IntParamNrm.CouponUnit) * Schedule.InterestParams.Nominal;
      FInterestRec.InterestPrm := 0;
      FInterestRec.Amount := FInterestRec.Interest;
      FInterestRec.Amount := RoundValue(FInterestRec.Amount);
    end;
    FInterestRec.BaseAmount := FInterestRec.Amount;
  end;}
end;


function TIntTimetable.GetAdjFactor(ADate: TDateTime): Extended;
begin
  Result := 1.0;
  if not FTranSchedule.InterestParams.IsAdjFactor then
    Exit;

  if FAdjFactorService<>nil then
    Result := FAdjFactorService.GetRate(ADate);
end;

function TIntTimetable.GetBusinessDay(D: TDateTime; AdjRule: char; ccy: integer): TDateTime;
var bdcs: TBusinessDayConvService;
bpol: IBusinessDayPolicy;
begin
  try
    bpol := TBusinessDayPolicyCcy.Create(FBusinessDayService,ccy);
    bdcs := TBusinessDayConvService.Create(bpol, Schedule.InterestParams.MaturityDate);
    Result := bdcs.GetBusinessDay(D, TBusinessDayConvService.CharToAdjRule(AdjRule));
  finally
    bdcs.Free;
    //bpol.Free;
  end;
end;


function TIntTimetable.GetCurrentInterestParamNrm: TInterestParamNrm;
begin
  Result := FTranSchedule.InterestParams.IntParamNrm;
end;

function TIntTimetable.GetInterestParamPrm: TInterestParamPrm;
begin
  Result := FTranSchedule.InterestParams.IntParamPrm;
end;

function TIntTimetable.GetInterestParams: TInterestParams;
begin
  Result := FTranSchedule.InterestParams;
end;

procedure TIntTimetable.GetIntDayFC(ADate: TDateTime; AdjRule: char; AInc: word; AUnit: char; var DateEnd, DateEvent: TDateTime);
var bdcs: TBusinessDayConvService;
bpol: IBusinessDayPolicy;
begin
  try
    bpol := TBusinessDayPolicyFC.Create(FBusinessDayService,ccy);
    bdcs := TBusinessDayConvService.Create(bpol, Schedule.InterestParams.MaturityDate);
    bdcs.GetIntDay(ADate, AdjRule, AInc, AUnit, IntParamNrm.FixingEom, FTranSchedule.InterestParams.IsEndEventDate, DateEnd, DateEvent);
  finally
    bdcs.Free;
    //bpol.Free;
  end;
end;

{procedure TIntTimetable.GetIntDayFCCcy(ADate: TDateTime; AdjRule: char; ccy: integer;
  AInc: word; AUnit: char; var DateEnd, DateEvent: TDateTime);
begin
  FBusinessDayService.InitFCCcy(ccy, False);
  FBusinessDayService.GetIntDay(ADate, AdjRule, AInc, AUnit, IntParamNrm.FixingEom, IsEndEventDate, DateEnd, DateEvent);
end;}

procedure TIntTimetable.SetFCS(AFCS: array of integer);
begin
  FBusinessDayService.SetFCArray(AFCS);//.AddFCPar(bdtFC,afcs[i]);
  FTransChedule.InterestParams.IsCcyFCS := True;
end;

procedure TIntTimetable.SetRoundD(ADecimals: Integer; ARndRT: boolean);
begin
  FRndRnd := 1/IntPower(10,ADecimals);
  FRndRT  := ARndRT;
end;


function TIntTimetable.GetInterestRate(AIntTable, ACcy, AIntPeriod: integer; ARateDate: TDateTime): extended;
begin
  Result := 0;
  FSession.ExecProc('RATE_UTL.GET_INTEREST_RATE', [AIntTable, ACcy, AIntPeriod, ARateDate]);
  Result := FSession.ParamByName('RESULT').AsFloat;
end;


procedure TIntTimetable.SetAmounts;
var
  i,j,ix: integer;
  xInterest, xInterest1,xNominal: extended;
  xRateNrmBase, xRatePrmBase,xRatePrm, xRate: extended;
  cnt: integer;
  //FRecSched,FRecSchedR: TRecSched;
  FInterestRec: TInterestRec;
  FRateRec: TRateRec;
begin
  xRateNrmBase := IntParamNrm.FixRate;
  xRateprm := 0;
  cnt := 0;

  for i := 0 to Schedule.InterestPaymentsCount-1 do
  begin
    FInterestRec := Schedule.InterestSched.InterestPayments[i];
    Schedule.SetCurrentInterestParamByPaymentStartDate(FInterestRec.StartDate);
    if FInterestRec.isDone then Continue;

    for j := 0 to FInterestRec.ResetCount-1 do
    begin
      FRateRec := FInterestRec.ResetItems[j];
      Inc(cnt);
      if FRateRec.isDone then Continue;
      //xNominal := GetNominal(FRecSched^.StartDate);
      FRateRec.Nominal := GetNominal(FRateRec);
      if (IntParamNrm.TypeOfIntRate = 'C') then
      begin
        xRateNrmBase := IntParamNrm.FixRate;
        xRate := xRateNrmBase;
      end
      else
      begin
        if (IntParamNrm.FixingType = 'D') or (IntParamNrm.FixingType = 'E') then
        begin
          xRateNrmBase := GetRateNrm(FRateRec.ResetDate, FRateRec.EndDate, FRateRec.FixingDate);
          xRate := xRateNrmBase * IntParamNrm.IntFactor + IntParamNrm.IntConst;
        end
        else
        begin
          if FRateRec.ResetDate <= Schedule.InterestParams.CurrentDate then
          begin
            xRateNrmBase := GetRateNrm(FRateRec.ResetDate, FRateRec.EndDate, FRateRec.FixingDate);
            xRate := xRateNrmBase * IntParamNrm.IntFactor + IntParamNrm.IntConst;
          end;
        end;
      end;
      FRateRec.Rate := xRate;
      if FRateRec.FixingType='E' then
      begin
        FRateRec.Interest := FRateRec.ExtAmount;
        FRateRec.Rate := FRateRec.ExtIntRate;
      end
      else
      begin
        FRateRec.Interest := Self.GetInterestAmountNrm(FRateRec.Nominal, FRateRec.Rate, FRateRec.ResetDate, FRateRec.EndDate);
      end;
      FRateRec.Amount := FRateRec.Interest;

      if (Schedule.InterestParams.IsDualAccrual) then
      begin
        FRateRec.NominalPrm := IntParamPrm.Nominal;
        if (IntParamPrm.TypeOfIntRate = 'C') then
        begin
          xRatePrmBase := IntParamPrm.FixRate;
          xRatePrm := xRatePrmBase;
        end
        else if (IntParamPrm.TypeOfIntRate = 'F') then
        begin
          if (IntParamNrm.FixingType = 'D') or (IntParamNrm.FixingType = 'E') then
          begin
            xRatePrmBase := GetRatePrm(FRateRec.ResetDate, FRateRec.EndDate, FRateRec.FixingDate);
            xRatePrm := xRatePrmBase * IntParamPrm.IntFactor + IntParamPrm.IntConst;
          end
          else
          begin
            if FRateRec.ResetDate <= Schedule.InterestParams.CurrentDate then
            begin
              xRatePrmBase := GetRatePrm(FRateRec.ResetDate, FRateRec.EndDate, FRateRec.FixingDate);
              xRatePrm := xRatePrmBase * IntParamPrm.IntFactor + IntParamPrm.IntConst;
            end;
          end;
        end;
        FRateRec.Rate1 := xRatePrm;
        FRateRec.InterestPrm := Self.GetInterestAmountPrm(FRateRec.NominalPrm, FRateRec.Rate1, FRateRec.ResetDate, FRateRec.EndDate);
        FRateRec.Amount := FRateRec.Interest+FRateRec.InterestPrm;
      end
      else
      begin
        FRateRec.Rate1 := 0;
        FRateRec.InterestPrm := 0;
        FRateRec.NominalPrm := 0;
      end;
      FRateRec.isChanged := True;
    end;
    //
    FInterestRec.AdjFactor := Self.GetAdjFactor(FInterestRec.EventDate);
    if FInterestRec.ResetCount=0 then
    begin
      FInterestRec.Nominal := GetNominal(FInterestRec);
      FInterestRec.Rate := IntParamNrm.FixRate;
      FInterestRec.Interest := Self.GetInterestAmountNrm(FInterestRec.Nominal, FInterestRec.Rate, FInterestRec.StartDate, FInterestRec.EndDate);
      if (Schedule.InterestParams.IsDualAccrual) then
      begin
        FInterestRec.NominalPrm := IntParamPrm.Nominal;
        FInterestRec.RatePrm := IntParamPrm.FixRate;
        FInterestRec.InterestPrm := Self.GetInterestAmountPrm(FInterestRec.NominalPrm, FInterestRec.RatePrm, FInterestRec.StartDate, FInterestRec.EndDate);
      end
      else
      begin
        FInterestRec.NominalPrm := 0;
        FInterestRec.RatePrm := 0;
        FInterestRec.InterestPrm := 0;
      end;
      FInterestRec.BaseAmount := FInterestRec.Interest + FInterestRec.InterestPrm;
      FInterestRec.Amount := FInterestRec.BaseAmount*FInterestRec.AdjFactor;
      FInterestRec.Amount := RoundValue(FInterestRec.Amount);
    end
    else
    begin
      xInterest := 0;
      xInterest1 := 0;
      for j := 0 to FInterestRec.ResetCount-1 do
      begin
        FRateRec := FInterestRec.ResetItems[j];
        if FRateRec.FixingType='E' then  //sztuczne podstawienie jesli wystapi zewnetrzny reset z K+ -> nie sumujemy resetow tylko pobieramy dane zewnetrzne
        begin
          xInterest := FRateRec.Interest;
          xInterest1 := FRateRec.InterestPrm;
          xNominal := FRateRec.Nominal;
          xRate := FRateRec.Rate;
          xRatePrm := FRateRec.Rate1;
          Break;
        end;
        if FTranSchedule.InterestParams.IsAdjFactor then
          xinterest := xinterest + FRateRec.Interest
        else
          xinterest := xinterest + RoundValue(FRateRec.Interest);

        xInterest1 := xInterest1 + FRateRec.InterestPrm;
        xNominal := FRateRec.Nominal;
        xRate := FRateRec.Rate;
        xRatePrm := FRateRec.Rate1;
      end;
      FInterestRec.BaseAmount := xInterest+xInterest1;
      FInterestRec.Amount := FInterestRec.BaseAmount*FInterestRec.AdjFactor;
      FInterestRec.Amount := RoundValue(FInterestRec.Amount);
      //
      FInterestRec.Interest := xInterest;
      FInterestRec.Rate  := xRate;
      FInterestRec.Nominal := xNominal;
      //
      FInterestRec.InterestPrm := xInterest1;
      FInterestRec.RatePrm := xRatePrm;
      FInterestRec.NominalPrm := IntParamPrm.Nominal;
    end;
    FInterestRec.isChanged := True;
  end;
end;

procedure TIntTimetable.SetAmountsCapFloor;
var
  i,j,ix: integer;
  xInterest, xNominal: extended;
  xRateBase, xRateX, xRate: extended;
  cnt: integer;
  //FRecSched,FRecSchedR: TRecSched;
  FInterestRec: TInterestRec;
  FRateRec: TRateRec;
begin
  cnt := 0;
  for i := 0 to Schedule.InterestPaymentsCount-1 do
  begin
    FInterestRec := Schedule.InterestSched.InterestPayments[i];
    Schedule.SetCurrentInterestParamByPaymentStartDate(FInterestRec.StartDate);
    if FInterestRec.isDone then Continue;

    for j := 0 to FInterestRec.ResetCount-1 do
    begin
      FRateRec := FInterestRec.ResetItems[j];
      Inc(cnt);
      if FRateRec.isDone then
      begin
        xRate := FInterestRec.Rate;
        Continue;
      end;
      //xNominal := GetNominal(FRecSched^.StartDate);
      FRateRec.Nominal := GetNominal(FRateRec);
      if (IntParamNrm.TypeOfIntRate = 'C') then
      begin
        xRateBase := IntParamNrm.FixRate;
        xRateX := xRateBase;
      end
      else
      begin
        if (IntParamNrm.FixingType = 'D') or (IntParamNrm.FixingType = 'E') then
        begin
          xRateBase := GetRateNrm(FRateRec.ResetDate, FRateRec.EndDate, FRateRec.FixingDate);
          xRateX := xRateBase * IntParamNrm.IntFactor + IntParamNrm.IntConst;
        end
        else
        begin
          if FRateRec.ResetDate <= Schedule.InterestParams.CurrentDate then
          begin
            xRateBase := GetRateNrm(FRateRec.ResetDate, FRateRec.EndDate, FRateRec.FixingDate);
            xRateX := xRateBase * IntParamNrm.IntFactor + IntParamNrm.IntConst;
          end;
        end;
      end;
      if FTranSchedule.InterestParams.CapFloorFactor = 1 then //transakcja CAP
      begin
        xRate := Max(xRateX - IntParamNrm.StrikeRate,0);
      end
      else //transakcja FLOOR
      begin
        xRate := Max(IntParamNrm.StrikeRate - xRateX,0);
      end;
      FRateRec.Rate := xRate;
      if FRateRec.FixingType='E' then
      begin
        FRateRec.Interest := FRateRec.ExtAmount;
        FRateRec.Rate := FRateRec.ExtIntRate;
      end
      else
      begin
        FRateRec.Interest := Self.GetInterestAmountNrm(FRateRec.Nominal, FRateRec.Rate, FRateRec.ResetDate, FRateRec.EndDate);
      end;
      FRateRec.Amount := FRateRec.Interest;

      FRateRec.Rate1 := 0;
      FRateRec.InterestPrm := 0;
      FRateRec.NominalPrm := 0;
      FRateRec.isChanged := True;
    end;
    //
    FInterestRec.AdjFactor := Self.GetAdjFactor(FInterestRec.EventDate);
    if FInterestRec.ResetCount=0 then
    begin
      FInterestRec.Nominal := GetNominal(FInterestRec);
      FInterestRec.Rate := IntParamNrm.FixRate;
      FInterestRec.Interest := Self.GetInterestAmountNrm(FInterestRec.Nominal, FInterestRec.Rate, FInterestRec.StartDate, FInterestRec.EndDate);
      FInterestRec.NominalPrm := 0;
      FInterestRec.RatePrm := 0;
      FInterestRec.InterestPrm := 0;
      FInterestRec.BaseAmount := FInterestRec.Interest;
      FInterestRec.Amount := FInterestRec.Interest;
    end
    else
    begin
      xInterest := 0;
      for j := 0 to FInterestRec.ResetCount-1 do
      begin
        FRateRec := FInterestRec.ResetItems[j];
        if FRateRec.FixingType='E' then  //sztuczne podstawienie jesli wystapi zewnetrzny reset z K+ -> nie sumujemy resetow tylko pobieramy dane zewnetrzne
        begin
          xInterest := FRateRec.Interest;
          xNominal := FRateRec.Nominal;
          xRate := FRateRec.Rate;
          Break;
        end;
        xinterest := xinterest + RoundValue(FRateRec.Interest);
        xNominal := FRateRec.Nominal;
        xRate := FRateRec.Rate;
      end;
      xInterest := RoundValue(xInterest);
      FInterestRec.BaseAmount := xInterest;
      FInterestRec.Amount := xInterest;
      //
      FInterestRec.Interest := xInterest;
      FInterestRec.Rate  := xRate;
      FInterestRec.Nominal := xNominal;
      //
      FInterestRec.InterestPrm := 0;
      FInterestRec.RatePrm := 0;
      FInterestRec.NominalPrm := IntParamPrm.Nominal;
    end;
    FInterestRec.isChanged := True;
  end;
end;

procedure TIntTimetable.SetFCTran(Tran: integer; art: char);
begin
  FBusinessDayService.setFCTran(Tran,art);
end;

procedure TIntTimetable.SetFCProd(Prod: integer);
begin
  FBusinessDayService.SetFCPar(bdtProd,Prod);
  
  //--JUrbaniak Przeniesienie zmian z F00291557.
  //AD w celu eliminacji b³êdu "Nie znaleziono parametru 'DA'" przy generowaniu harmonogramu na emisji
  if FBusinessDayService.IsEmptyFC then
    FBusinessDayService.AddFCPar(bdtCcy,ccy);
  FBusinessDayService.Prepare;
end;

function TIntTimetable.GetNextDayFC(Adate: TDateTime; days: integer): TDateTime;
begin
  result := FBusinessDayService.GetNextDay(Adate, days);
end;

{function TIntTimetable.GetNominal(ADate: TDateTime): extended;
var
FNominalRec: TNominalRec;
begin
  Result := FNominal;
  FNominalRec := Schedule.GetNominal(ADate);
  if FNominalRec<>nil then
    Result := FNominalRec.Nominal;
end;}

function TIntTimetable.GetNominal(AInterestRec: TInterestRec): extended;
var
FNominalRec: TNominalRec;
begin
  Result := Schedule.InterestParams.Nominal;
  if NominalResetDate=nrStart then
  begin
    FNominalRec := Schedule.NominalSched.GetLastReset(AInterestRec.StartDate);
    if FNominalRec<>nil then
      Result := FNominalRec.Nominal;
  end
  else
  begin
    FNominalRec := Schedule.NominalSched.GetLastReset(AInterestRec.EndDate);
    if FNominalRec<>nil then
      Result := FNominalRec.Nominal;
  end;
end;

function TIntTimetable.GetNominal(AResetRec: TRateRec): extended;
var
FNominalRec: TNominalRec;
begin
  Result := Schedule.InterestParams.Nominal;
  if NominalResetDate=nrStart then
  begin
    FNominalRec := Schedule.NominalSched.GetLastReset(AResetRec.ResetDate);
    if FNominalRec<>nil then
      Result := FNominalRec.Nominal;
  end
  else
  begin
    FNominalRec := Schedule.NominalSched.GetLastReset(AResetRec.EndDate);
    if FNominalRec<>nil then
      Result := FNominalRec.Nominal;
  end;
end;

function TIntTimetable.GetNominal(AStartDate, AEndDate: TDateTime): extended;
var
FNominalRec: TNominalRec;
begin
  Result := Schedule.InterestParams.Nominal;
  if not Schedule.NominalSched.IsReset then
    exit;

  if NominalResetDate=nrStart then
  begin
    FNominalRec := Schedule.NominalSched.GetLastReset(AStartDate);
    if FNominalRec<>nil then
      Result := FNominalRec.Nominal;
  end
  else
  begin
    FNominalRec := Schedule.NominalSched.GetLastReset(AEndDate);
    if FNominalRec<>nil then
      Result := FNominalRec.Nominal;
  end;
end;

procedure TIntTimetable.Unprepare;
begin
end;

procedure TIntTimetable.Prepare;
begin
  if FBusinessDayService.IsEmptyFC then
    FBusinessDayService.AddFCPar(bdtCcy,ccy);
  FBusinessDayService.Prepare;
  FYieldCurveService.Prepare;
end;

function TIntTimetable.RoundValue(AValue: Extended): Extended;
begin
  Result :=IfNumUtils.RoundVal(AValue,FRndRnd,FRndRT);
end;

function TIntTimetable.GetRate(StartDate, EndDate, FixingDate: TDateTime;
  AIntTable, AIntPeriod: integer): extended;
begin
  Result := 0;
  if StartDate <= Schedule.InterestParams.CurrentDate then
  begin
    Result := GetInterestRate(AIntTable, Fccy, AIntPeriod, FixingDate);
  end
  else
  begin
    if (IntParams.YieldCurve > 0) then
      Result := FYieldCurveService.GetYieldFRADD(IntParams.YieldCurve, Fccy, Schedule.InterestParams.CurrentDate, StartDate, EndDate + 1)
    else
      Result := GetInterestRate(AIntTable, Fccy, AIntPeriod, FixingDate);
  end;
end;

function TIntTimetable.GetRateNrm(StartDate, EndDate, FixingDate: TDateTime): extended;
begin
  Result := GetRate(StartDate, EndDate, FixingDate, IntParamNrm.IntTableId, IntParamNrm.IntPeriodId);
end;


function TIntTimetable.GetRatePrm(StartDate, EndDate, FixingDate: TDateTime): extended;
begin
  Result := GetRate(StartDate, EndDate, FixingDate, IntParamPrm.IntTableId, IntParamPrm.IntPeriodId);
end;

function TIntTimetable.GetFixingDateFC(ADate: TDateTime): TDateTime;
begin
  if IntParamNrm.FixingAdjust then
  begin
    Result := FBusinessDayService.GetNextDay(ADate+1,-1);
  end
  else
  begin
    Result := ADate;
  end;
end;

function TIntTimetable.GetInterestAmountNrm(Amount, Rate: extended; Date1, Date2: TDateTime): extended;
begin
  Result := GetInterestAmount(Amount, Rate, Date1, Date2, Schedule.InterestParams.MaturityDate, IntParamNrm.Basis, IntParamNrm.CouponInc, IntParamNrm.CouponUnit, IntParamNrm.FixingEOM, IntParamNrm.UserBasisDays, IntParamNrm.UserBasisBasis);
end;

function TIntTimetable.GetInterestAmountPrm(Amount, Rate: extended; Date1, Date2: TDateTime): extended;
begin
Result := GetInterestAmount(Amount, Rate, Date1, Date2, Schedule.InterestParams.MaturityDate, IntParamPrm.Basis, IntParamNrm.CouponInc, IntParamNrm.CouponUnit, IntParamNrm.FixingEOM,
  IntParamPrm.UserBasisDays, IntParamPrm.UserBasisBasis);
end;

procedure TIntTimetable.SetCcy(accy: Integer);
begin
  //IntParams.CcyId := accy;
  try
    FCcy := Accy;
    FSession.ExecProc('CCY_UTL.GET_CCY_RND',[FCcy,FRndRnd,FRndRt]);
    FRndRnd := FSession.ParamByName('RND').AsFloat;
    FRndRT := FSession.ParamByName('RT').AsBoolean;
  except
    FRndRnd := 0.01;
    FRndRT  := True;
  end;
end;

function TIntTimetable.SetCurrentInterestParam(ADate: TDateTime): TInterestParamNrm;
begin
  Result := FTranSchedule.InterestParams.SetCurrentInterestParam(ADate);
end;

procedure TIntTimetable.SetDependentsSession(DB: TOraSession);
begin
  FBusinessDayService.Session := DB;
  FYieldCurveService.Session := DB;
end;

procedure TIntTimetable.SaveSchedTmp(AType: char);
var
i,j: Integer;
bN,bI,bR: Boolean;
spProc: TOraStoredProc;
//FRecSched: TRecSched;
FInterestRec: TInterestRec;
FNominalRec: TNominalRec;
FRateRec: TRateRec;
begin
  spProc:=TOraStoredProc.Create(nil);
  spProc.Session:=FSession;
  spProc.StoredProcName:='INT_SCHED_UTL.CLEAR';
  spProc.ExecProc;
  bN := False;
  bI := False;
  bR := False;
  bn := ((AType='N') or (AType=#0)) and (Schedule.NominalSched.Count > 0);
  bi := ((AType='I') or (AType=#0)) and (Schedule.InterestPaymentsCount > 0);
  if (AType='R') or (AType=#0) then
  begin
    for i := 0 to Schedule.InterestPaymentsCount-1 do
    begin
      if Schedule.InterestSched.InterestPayments[i].ResetCount>0 then
      begin
        bR := True;
        Break;
      end;
    end;
  end;
  if bN then
  begin
    spProc.StoredProcName:='INT_SCHED_UTL.ADD_NOMINAL_RESET';
    spProc.Prepare;
    for i := 0 to Schedule.NominalSched.Count-1 do
    begin
      FNominalRec := Schedule.NominalSched[i];
      spProc.Params[0].AsString := FNominalRec.Key;
      spProc.Params[1].AsDateTime := FNominalRec.ResetDate;
      spProc.Params[2].AsString := FNominalRec.RT1;
      spProc.Params[3].AsString := FNominalRec.ChangeType;
      spProc.Params[4].AsFloat := FNominalRec.Nominal;
      spProc.Params[5].AsFloat := FNominalRec.NominalChange;
      spProc.Params[6].AsInteger := 0;
      if FNominalRec.isDone then
        spProc.Params[7].AsInteger := 1
      else
        spProc.Params[7].AsInteger := 0;
      spProc.Execute;
    end;
    spProc.UnPrepare;
  end;
  if bR then
  begin
    spProc.StoredProcName:='INT_SCHED_UTL.ADD_RATE_RESET';
    spProc.Prepare;
    for i := 0 to Schedule.InterestPaymentsCount-1 do
    begin
      FInterestRec := Schedule.InterestSched.InterestPayments[i];
      for j := 0 to FInterestRec.ResetCount - 1 do
      begin
        FRateRec := FInterestRec.ResetItems[j];
        spProc.Params[0].AsString := FRateRec.Key;
        spProc.Params[1].AsString := FRateRec.RT1;
        spProc.Params[2].AsDateTime := FRateRec.ResetDate;
        spProc.Params[3].AsDateTime := FRateRec.EndDate;
        spProc.Params[4].AsDateTime := FRateRec.FixingDate;
        spProc.Params[5].AsFloat := FRateRec.Rate;
        spProc.Params[6].AsFloat := FRateRec.Rate1;
        spProc.Params[7].AsFloat := FRateRec.Nominal;
        spProc.Params[8].AsFloat := FRateRec.NominalPrm;
        spProc.Params[9].AsFloat := FRateRec.Interest;
        spProc.Params[10].AsFloat := FRateRec.InterestPrm;
        spProc.Params[11].AsFloat := FRateRec.Amount;
        spProc.Params[12].AsInteger := IFDBUtils.Bool2Int(FRateRec.isDone);
        spProc.Execute;
      end;
    end;
    spProc.UnPrepare;
  end;
  if bI then
  begin
    spProc.StoredProcName:='INT_SCHED_UTL.ADD_INTEREST_PAYMENT';
    spProc.Prepare;
    for i := 0 to Schedule.InterestPaymentsCount-1 do
    begin
      //to do adj_factor
      FInterestRec := Schedule.InterestSched.InterestPayments[i];
      spProc.Params[0].AsString := FInterestRec.Key;
      spProc.Params[1].AsDateTime := FInterestRec.StartDate;
      spProc.Params[2].AsDateTime := FInterestRec.EndDate;
      spProc.Params[3].AsDateTime := FInterestRec.EventDate;
      spProc.Params[4].AsFloat := FInterestRec.Nominal;
      spProc.Params[5].AsFloat := FInterestRec.NominalPrm;
      spProc.Params[6].AsFloat := FInterestRec.Amount;
      spProc.Params[7].AsFloat := FInterestRec.Rate;
      spProc.Params[8].AsString := FInterestRec.CashflowType;
      spProc.Params[9].AsInteger := IFDBUtils.Bool2Int(FInterestRec.isDone);
      spProc.Execute;
    end;
    spProc.UnPrepare;
  end;
  spProc.Free;
end;

function TIntTimetable.UpdateScheduleAnnuity: Boolean;
var ret:integer;
begin
  Result:=False;
  ret:=Generate;
  //Show;
  Result:= ret<>0;
end;

function TIntTimetable.UpdateSchedule: Boolean;
var i,j,k: Integer;
b: Boolean;
dEnd,d1,d2: TDateTime;
xINominal,xIInterest,xIInterestPrm,xIRate: Extended;
xRNominal: Extended;
xRRateOld,xRRate,xRInterest,xRInterestPrm,xRRRR, xBaseAmount: Extended;
RI: TInterestRec;
RR: TRateRec;
RN: TNominalRec;
begin
  Result := False;
  if UpdateResetN then
    Result := True;
  Schedule.SortSchedule;
  //Ustalenie daty poczatkowej i koncowej platnosci
  //jeszcze powinna byc petelka
  for I := 0 to Schedule.InterestPaymentsCount - 1 do
  begin
    RI := Schedule.InterestSched.InterestPayments[i];
    if RI.ResetCount>0 then
    begin
      RR := RI.ResetItems[0];
      if RR.ResetDate<>RI.StartDate then
      begin
        RR.ResetDate := RI.StartDate;
      end;
      RR := RI.ResetItems[RI.ResetCount-1];
      if RR.EndDate<>RI.EndDate then
      begin
        RR.EndDate := RI.EndDate;
      end;
    end;
  end;

  {if Schedule.InterestPaymentsCount>0 then
  begin
    RI := Schedule.InterestSched.InterestPayments[Schedule.InterestPaymentsCount-1];
    if RI.ResetCount > 0 then
    begin
      RR := RI.ResetItems[RI.ResetCount-1];
      if RR.EndDate<>Schedule.InterestParams.MaturityDate-1 then
      begin
        RR.EndDate := Schedule.InterestParams.MaturityDate-1;
      end;
    end;
  end;}
  for i := 0 to Schedule.InterestPaymentsCount - 1 do
  begin
    RI := Schedule.InterestSched.InterestPayments[i];
    Schedule.InterestParams.SetCurrentInterestParam(ri.StartDate);
    xIInterest := 0;
    xIInterestPrm := 0;
    xIRate :=0;

    if RI.ResetCount=0 then
    begin
      xINominal := GetNominal(RI);
      //function TIntTimetable.GetInterestAmountNrm(Amount, Rate: extended; Date1, Date2: TDateTime): extended;
      xIInterest := GetInterestAmountNrm(
        xINominal,
        RI.Rate,
        RI.StartDate,
        RI.EndDate);
      xIRate :=RI.Rate;
      if not Schedule.InterestParams.IsDualAccrual then
        xIInterestPrm := 0
      else
        xIInterestPrm := GetInterestAmountPrm(
          IntParamPrm.Nominal,
          RI.RatePrm,
          RI.StartDate,
          RI.EndDate);
    end
    else
    begin
      for j := 0 to RI.ResetCount - 1 do
      begin
        RR := RI.ResetItems[j];
        xRNominal := GetNominal(rr);
        //
        if RR.FixingType='E' then
          xRRateOld := RR.ExtIntRate
        else
          xRRateOld := RR.Rate;

        xRRRR:= RR.Rate;
        if RR.RT1='R' then
        begin
          xRRate := xRRateOld;
          xRRRR := xRRateOld;
        end
        else
        begin
          xRRate := xRRRR;
        end;
        if RR.FixingType='E' then
          xRInterest := RR.ExtAmount
        else
          xRInterest := GetInterestAmountNrm(
            xRNominal,
            xRRate,
            RR.ResetDate,
            RR.EndDate);
        if not Schedule.InterestParams.IsDualAccrual then
          xRInterestPrm := 0
        else
          xRInterestPrm := GetInterestAmountPrm(
            IntParamPrm.Nominal,
            RR.Rate1,
            RR.ResetDate,
            RR.EndDate);
        if not (FltEqual(RR.Nominal,xRNominal,2) and
              FltEqual(RR.Interest,xRInterest,2) and
              FltEqual(RR.InterestPrm,xRInterestPrm,2)) then
        begin
          RR.Nominal := xRNominal;
          RR.NominalPrm := IntParamPrm.Nominal;
          RR.Rate := xRRate;
          RR.Interest := xRInterest;
          RR.InterestPrm := xRInterestPrm;
          RR.Amount := xRInterest+xRInterestPrm;
          RR.isChanged := True;
          Result := True;
        //TRecSched(FArrSched[i]^).bChanged1 := True;
        end;

        //
        //SUMOWANIE
        if RR.FixingType='E' then //sztuczne podstawienie jesli wystapi zewnetrzny reset z K+ -> nie sumujemy resetow tylko pobieramy dane zewnetrzne
        begin
          xIInterest  := RR.Interest;
          xIInterestPrm := RR.InterestPrm;
          xINominal  := RR.Nominal;
          xIRate  := RR.Rate;
          Break;
        end;
        if FTranSchedule.InterestParams.IsAdjFactor then
          xIInterest := xIInterest + RR.Interest
        else
          xIInterest := xIInterest + RoundValue(RR.Interest);


        xIInterestPrm := xIInterestPrm + RR.InterestPrm;
        xINominal  := RR.Nominal;
        xIRate  := RR.Rate;
      end;
    end;
    xBaseAmount := xIInterest+xIInterestPrm;
    if not (FltEqual(RI.Nominal,xINominal,2) and
            FltEqual(RI.Interest,xIInterest,2) and
            FltEqual(RI.InterestPrm,xIInterestPrm,2) and
            FltEqual(RI.BaseAmount,xBaseAmount,2)) then
    begin
      RI.Nominal := xINominal;
      RI.Rate := xIRate;
      RI.Interest := xIInterest;
      RI.InterestPrm := xIInterestPrm;
      RI.BaseAmount := xBaseAmount;
      RI.Amount := RI.BaseAmount*RI.AdjFactor;
      RI.isChanged := True;
      Result := True;
      //TRecSched(FArrSched[i]^).bChanged1 := True;
    end;
    //sumowanie
  end;
end;


procedure TIntTimetable.Show;
var lst: TStrings;
i,j: Integer;
//a: TRecSched;
rn: TNominalRec;
ri: TInterestRec;
rr: TRateRec;
s: string;
begin
  lst := TStringList.Create;
  for i := 0 to Schedule.NominalSched.Count-1 do
  begin
    rn := Schedule.NominalSched[i];
    s := '';
    AddStr(s,'N,' + rn.rt1 + '#' +
      FormatDateTime('"EV="yyyy/mm/dd, ',rn.ResetDate));
      //FormatDateTime('"X1="yyyy/mm/dd, ',rn.XDATE1)+
      //FormatDateTime('"X2="yyyy/mm/dd, ',rn.XDATE2));
    AddStr(s,Format('CT=%s C=%f N=%f',[rn.ChangeType,rn.NominalChange,rn.Nominal]),' ');
    if rn.isChanged then
      AddStr(s,'chg',' ');
    lst.Add(s);
  end;
  lst.Add('### Interest');
  for I := 0 to Schedule.InterestPaymentsCount - 1 do
  begin
    ri := Schedule.InterestSched.InterestPayments[i];
    for j := 0 to ri.ResetCount - 1 do
    begin
      rr := ri.ResetItems[j];
      s := '';
      AddStr(s,'$R,' + rn.rt1 + '#' +
        FormatDateTime('"EV="yyyy/mm/dd, ',rr.ResetDate)+
        FormatDateTime('"ED="yyyy/mm/dd, ',rr.EndDate));
        //FormatDateTime('"X1="yyyy/mm/dd, ',rr.XDATE1)+
        //FormatDateTime('"X2="yyyy/mm/dd, ',rr.XDATE2));
      AddStr(s,'key='+rr.Key + ' ' + FormatDateTime('"keyd="yyyy/mm/dd, ',rr.KeyDate));
      AddStr(s,Format('R=%f ',[rr.Rate])+Format('A=%f ',[rr.Amount])+Format('N=%f ',[rr.Nominal])+Format('I=%f ', [rr.Interest]),' ');
      if rr.isChanged then
        AddStr(s,'chg',' ');
      lst.Add(s);
    end;
    s := '';
    AddStr(s,'$I,' + rn.rt1 + '#' +
      FormatDateTime('"EV="yyyy/mm/dd, ',ri.EventDate)+
      FormatDateTime('"SD="yyyy/mm/dd, ',ri.StartDate)+
      FormatDateTime('"ED="yyyy/mm/dd, ',ri.EndDate));
      //FormatDateTime('"X1="yyyy/mm/dd, ',ri.XDATE1)+
      //FormatDateTime('"X2="yyyy/mm/dd, ',ri.XDATE2));
    AddStr(s,'key='+ri.Key + ' ' + FormatDateTime('"keyd="yyyy/mm/dd, ',ri.KeyDate));
    //AddStr(s,Format('CT=%s C=%f N=%f',[ri.ChangeType,ri.NominalChange,rn.Nominal]),' ');
    AddStr(s,Format('R=%f ',[ri.Rate])+Format('A=%f ',[ri.Amount])+Format('N=%f ',[ri.Nominal])+Format('I=%f ', [ri.Interest]),' ');
    if rn.isChanged then
      AddStr(s,'chg',' ');
    lst.add(s);
  end;
  //ShowMessage(lst.Text);
  TextDlg.ShowDetailDialog(dlgInformation,'Schedule',lst.Text);
  lst.Free;
end;


function TIntTimetable.GetTimetableInternal(AReset: boolean): integer;
begin

end;

function TIntTimetable.UpdateDSRate(ADS: TDataSet; Mode: TUpdateMode): Boolean;
var
converter: IScheduleTODS;
begin
  converter := TRateResetScheduleToDS.Create(ADS,Schedule,IntParamNrm.FixingType);
  Result := converter.Update(mode);
end;

function TIntTimetable.UpdateDSInt(ADS: TDataSet; Mode: TUpdateMode):Boolean;
var
converter: IScheduleTODS;
begin
  //bRateFixed := (IntParamNrm.TypeOfIntRate='C') and ((IntParamPrm.TypeOfIntRate='C') or (IntParamPrm.TypeOfIntRate=#0));
  converter := TInterestScheduleToDS.Create(ADS,Schedule,InterestKey,Schedule.InterestParams.IntSchedAfterZeroNominal,IntParams.IsNegativeInterest,Schedule.InterestParams.CashflowNormalDirection);
  Result := converter.Update(mode);
end;

function TIntTimetable.UpdateDSNominal(ADS: TDataSet): Boolean;
var
converter: IScheduleTODS;
begin
  converter := TNominalScheduleToDS.Create(ADS,Schedule);
  Result := converter.Update(umInsertUpdate);
end;

function TIntTimetable.GetIsNominalReset: Boolean;
begin
  Result := FTranSchedule.NominalSched.IsReset;
end;

function TIntTimetable.GetIsRateOrNominalReset: Boolean;
begin
  Result := (IntParamNrm.TypeOfIntRate='F') or (IntParamPrm.TypeOfIntRate='F') or (Schedule.NominalSched.IsReset);
end;

function TIntTimetable.UpdateSchedule2: Boolean;
var
  i: integer;
  spProc: TOraStoredProc;
  IPARXML: ixmldocument;
  ms: TMemoryStream;
begin
  Prepare;
  Result:=False;
  if Schedule.InterestParams.StartDateX = 0 then
    Schedule.InterestParams.StartDateX := Schedule.InterestParams.StartDate;
  if Schedule.NominalSched.LastNominalDate=0 then
    Schedule.NominalSched.LastNominalDate := Schedule.InterestParams.MaturityDate;
  SaveSchedTmp(#0);
  iparxml := IntParToXML;
  //ms := TMemoryStream.Create;
  //XMLSaveToStream(iparxml,ms,ofIndent);
  spProc:=TOraStoredProc.Create(nil);
  spProc.Session:=FSession;
  spProc.StoredProcName:='INT_SCHED_UTL.SET_AMOUNTS';
  spProc.Prepare;
  spProc.Params[1].AsString := XMLSaveToString(iparxml);
  spProc.Execute;
  spProc.UnPrepare;
  ClearSchedule;
  LoadScheduleFromTmpTable;
  Result := True;
  //ms.Free
end;


end.
