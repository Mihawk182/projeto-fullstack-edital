import { BehaviorSubject } from "rxjs";

export type NotificationItem = {
  id: string;
  message: string;
};

class NotificationFacade {
  private readonly subject = new BehaviorSubject<NotificationItem[]>([]);
  notifications$ = this.subject.asObservable();

  add(message: string) {
    const id = `${Date.now()}-${Math.random().toString(16).slice(2)}`;
    const next = [...this.subject.value, { id, message }].slice(-3);
    this.subject.next(next);

    setTimeout(() => {
      this.remove(id);
    }, 6000);
  }

  remove(id: string) {
    const next = this.subject.value.filter((item) => item.id !== id);
    this.subject.next(next);
  }
}

export const notificationFacade = new NotificationFacade();
