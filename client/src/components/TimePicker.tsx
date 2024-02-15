import React from 'react';

type Props = {
  onTimeChange: (time: string) => void;
  id: string;
  className?: string;
  time: string;
};

function TimePicker({ onTimeChange, id, className, time }: Props) {

  const handleHourChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    onTimeChange(event.target.value);
  };

  return (
    <select
      className={`
        text-center
        ${className}
      `}
      id={id}
      value={time}
      onChange={handleHourChange} >
      {
        Array.from({ length: 24 }, (_, i) => {
          const hour = (i + 1).toString().padStart(2, '0');
          return <option key={hour} value={hour}>{hour}</option>;
        })
      }
    </select >
  );
}

export default TimePicker;
